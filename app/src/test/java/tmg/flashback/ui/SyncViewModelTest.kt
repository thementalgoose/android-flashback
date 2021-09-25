package tmg.flashback.ui

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.common.controllers.ForceUpgradeController
import tmg.configuration.controllers.ConfigController
import tmg.flashback.managers.appshortcuts.AppShortcutManager
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.upnext.controllers.UpNextController
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SyncViewModelTest: BaseTest() {

    private var mockAppShortcutManager: AppShortcutManager = mockk(relaxed = true)
    private var mockRssController: RSSController = mockk(relaxed = true)
    private var mockConfigurationManager: ConfigController = mockk(relaxed = true)
    private var mockForceUpgradeController: ForceUpgradeController = mockk(relaxed = true)
    private var mockUpNextController: UpNextController = mockk(relaxed = true)

    private lateinit var sut: SyncViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockAppShortcutManager.enable() } returns true
        every { mockAppShortcutManager.disable() } returns true
        every { mockForceUpgradeController.shouldForceUpgrade } returns false
        every { mockRssController.enabled } returns false
        coEvery { mockConfigurationManager.fetchAndApply() } returns true
    }

    private fun initSUT() {
        sut = SyncViewModel(mockAppShortcutManager, mockRssController, mockConfigurationManager, mockForceUpgradeController, mockUpNextController)
    }

    @Test
    fun `starting sync shows loading`() = coroutineTest {
        initSUT()
        sut.inputs.start()

        sut.outputs.showLoading.test {
            assertValue(true)
        }
    }

    @Test
    fun `starting sync hides resync`() = coroutineTest {
        initSUT()
        sut.inputs.start()

        sut.outputs.showResync.test {
            assertValue(false)
        }
    }

    @Test
    fun `starting sync ensures cache is reset`() = coroutineTest {
        initSUT()
        sut.inputs.start()

        coVerify { mockConfigurationManager.ensureCacheReset() }
    }

    @Test
    fun `starting sync with rss enabled shows shortcut manager`() = coroutineTest {
        every { mockRssController.enabled } returns true

        initSUT()
        sut.inputs.start()

        verify { mockUpNextController.scheduleNotifications() }
        verify { mockAppShortcutManager.enable() }
    }

    @Test
    fun `starting sync with rss disabled shows shortcut manager`() = coroutineTest {
        every { mockRssController.enabled } returns false

        initSUT()
        sut.inputs.start()

        verify { mockUpNextController.scheduleNotifications() }
        verify { mockAppShortcutManager.disable() }
    }

    @Test
    fun `starting sync if fetch and apply fails show resync and loading`() = coroutineTest {
        every { mockRssController.enabled } returns false
        coEvery { mockConfigurationManager.fetchAndApply() } returns false

        initSUT()
        sut.inputs.start()

        sut.outputs.showResync.test {
            assertValue(true)
        }
        sut.outputs.showLoading.test {
            assertValue(false)
        }
    }

    @Test
    fun `starting sync if fetch and apply success but shows force upgrade`() = coroutineTest {
        every { mockRssController.enabled } returns false
        coEvery { mockConfigurationManager.fetchAndApply() } returns true
        every { mockForceUpgradeController.shouldForceUpgrade } returns true

        initSUT()
        sut.inputs.start()

        advanceUntilIdle()

        sut.outputs.goToForceUpgrade.test {
            assertEventFired()
        }
    }

    @Test
    fun `starting sync if fetch and apply success and no force upgrade then go to dashboard`() = coroutineTest {
        every { mockRssController.enabled } returns false
        coEvery { mockConfigurationManager.fetchAndApply() } returns true
        every { mockForceUpgradeController.shouldForceUpgrade } returns false

        initSUT()
        sut.inputs.start()

        advanceUntilIdle()

        sut.outputs.goToDashboard.test {
            assertEventFired()
        }
    }
}