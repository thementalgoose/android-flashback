package tmg.flashback.ui.dashboard

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.common.controllers.ForceUpgradeController
import tmg.configuration.controllers.ConfigController
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.managers.appshortcuts.AppShortcutManager
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.upnext.controllers.UpNextController
import tmg.testutils.BaseTest

internal class HomeViewModelTest: BaseTest() {

    private var mockAppShortcutManager: AppShortcutManager = mockk(relaxed = true)
    private var mockRssController: RSSController = mockk(relaxed = true)
    private var mockConfigurationManager: ConfigController = mockk(relaxed = true)
    private var mockCrashController: CrashController = mockk(relaxed = true)
    private var mockForceUpgradeController: ForceUpgradeController = mockk(relaxed = true)
    private var mockUpNextController: UpNextController = mockk(relaxed = true)

    private lateinit var sut: HomeViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockAppShortcutManager.enable() } returns true
        every { mockAppShortcutManager.disable() } returns true
        coEvery { mockConfigurationManager.applyPending() } returns true
        every { mockConfigurationManager.requireSynchronisation } returns false
        every { mockForceUpgradeController.shouldForceUpgrade } returns false
        every { mockRssController.enabled } returns false
    }

    private fun initSUT() {
        sut = HomeViewModel(
            mockConfigurationManager,
            mockRssController,
            mockCrashController,
            mockForceUpgradeController,
            mockAppShortcutManager,
            mockUpNextController
        )
    }

    @Test
    fun `initialise with config requiring sync notifies require sync`() = coroutineTest {
        every { mockConfigurationManager.requireSynchronisation } returns true

        initSUT()
        sut.initialise()

        assertTrue(sut.requiresSync)
        assertFalse(sut.forceUpgrade)
        assertTrue(sut.appliedChanges)
    }

    @Test
    fun `initialise with force upgrade true notifies force upgrade`() = coroutineTest {
        every { mockForceUpgradeController.shouldForceUpgrade } returns true

        initSUT()
        sut.initialise()

        assertFalse(sut.requiresSync)
        assertTrue(sut.forceUpgrade)
        assertTrue(sut.appliedChanges)
    }

    @Test
    fun `initialise with no force upgrade or sync with rss disabled notifies applied changes`() = coroutineTest {
        every { mockRssController.enabled } returns false

        initSUT()
        sut.initialise()

        coVerify { mockConfigurationManager.applyPending() }
        verify { mockAppShortcutManager.disable() }
        verify { mockUpNextController.scheduleNotifications() }

        assertFalse(sut.requiresSync)
        assertFalse(sut.forceUpgrade)
        assertFalse(sut.appliedChanges)
    }

    @Test
    fun `initialise with no force upgrade or sync with rss enabled notifies applied changes`() = coroutineTest {
        every { mockRssController.enabled } returns true

        initSUT()
        sut.initialise()

        coVerify { mockConfigurationManager.applyPending() }
        verify { mockAppShortcutManager.enable() }
        verify { mockUpNextController.scheduleNotifications() }

        assertFalse(sut.requiresSync)
        assertFalse(sut.forceUpgrade)
        assertFalse(sut.appliedChanges)
    }
}