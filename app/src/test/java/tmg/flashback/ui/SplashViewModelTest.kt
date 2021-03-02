package tmg.flashback.ui

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.core.controllers.FeatureController
import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.core.model.ForceUpgrade
import tmg.flashback.managers.appshortcuts.AppShortcutManager
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.assertEventNotFired
import tmg.flashback.testutils.test

internal class  SplashViewModelTest: BaseTest() {

    private var mockAppShortcutManager: AppShortcutManager = mockk(relaxed = true)
    private var mockFeatureController: FeatureController = mockk(relaxed = true)
    private var mockConfigurationManager: ConfigurationController = mockk(relaxed = true)

    private lateinit var sut: SplashViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockAppShortcutManager.enable() } returns true
        every { mockAppShortcutManager.disable() } returns true
        every { mockConfigurationManager.forceUpgrade } returns null
    }

    private fun initSUT() {
        sut = SplashViewModel(mockAppShortcutManager, mockFeatureController, mockConfigurationManager)
    }

    @Test
    fun `show resync is default to false and show loading default to false`() = coroutineTest {

        initSUT()

        sut.outputs.showLoading.test {
            assertValue(false)
        }
        sut.outputs.showResync.test {
            assertValue(false)
        }
    }

    @Test
    fun `start fetch and activate enables show loading`() = coroutineTest {

        every { mockConfigurationManager.requireSynchronisation } returns true
        coEvery { mockConfigurationManager.fetchAndApply() } returns true

        initSUT()
        sut.inputs.start()

        sut.outputs.showLoading.test {
            assertValue(true)
        }
    }

    @Test
    fun `start fetch and activate disables show resync`() = coroutineTest {

        every { mockConfigurationManager.requireSynchronisation } returns true
        coEvery { mockConfigurationManager.fetchAndApply() } returns true

        initSUT()
        sut.inputs.start()

        sut.outputs.showResync.test {
            assertValue(false)
        }
    }

    @Test
    fun `start fetch and activate calls remote config repo`() = coroutineTest {

        every { mockConfigurationManager.requireSynchronisation } returns true
        coEvery { mockConfigurationManager.fetchAndApply() } returns true

        initSUT()
        sut.inputs.start()

        coVerify { mockConfigurationManager.fetchAndApply() }
    }

    @Test
    fun `start fetch and activate when success sets updates shortcut manager`() = coroutineTest {

        every { mockConfigurationManager.requireSynchronisation } returns true
        every { mockFeatureController.rssEnabled } returns true
        coEvery { mockConfigurationManager.fetchAndApply() } returns true

        initSUT()
        sut.inputs.start()

        verify { mockAppShortcutManager.enable() }
    }

    @Test
    fun `start fetch and activate when success it fires go to next screen`() = coroutineTest {

        every { mockConfigurationManager.requireSynchronisation } returns true
        coEvery { mockConfigurationManager.fetchAndApply() } returns true

        initSUT()
        sut.inputs.start()

        sut.outputs.goToDashboard.test {
            assertEventFired()
        }
        sut.outputs.goToForceUpgrade.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `start fetch and activate when success it fires go to force upgrade if force upgrade exists`() = coroutineTest {

        every { mockConfigurationManager.forceUpgrade } returns ForceUpgrade("upgrade", "please", null)
        every { mockConfigurationManager.requireSynchronisation } returns true
        coEvery { mockConfigurationManager.fetchAndApply() } returns true

        initSUT()
        sut.inputs.start()

        sut.outputs.goToDashboard.test {
            assertEventNotFired()
        }
        sut.outputs.goToForceUpgrade.test {
            assertEventFired()
        }
    }

    @Test
    fun `start fetch and activate when failure then show resync sets to true`() = coroutineTest {

        every { mockConfigurationManager.requireSynchronisation } returns true
        coEvery { mockConfigurationManager.fetchAndApply() } returns false

        initSUT()
        sut.inputs.start()

        sut.outputs.showResync.test {
            assertValue(true)
        }
        sut.outputs.goToDashboard.test {
            assertEventNotFired()
        }
        sut.outputs.goToForceUpgrade.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `start fetch and activate when failure then show loading is set to false`() = coroutineTest {

        every { mockConfigurationManager.requireSynchronisation } returns true
        coEvery { mockConfigurationManager.fetchAndApply() } returns false

        initSUT()
        sut.inputs.start()

        sut.outputs.showLoading.test {
            assertValue(false)
        }
        sut.outputs.goToDashboard.test {
            assertEventNotFired()
        }
        sut.outputs.goToForceUpgrade.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `start activate then remote config activate called`() = coroutineTest {

        every { mockConfigurationManager.requireSynchronisation } returns false
        coEvery { mockConfigurationManager.applyPending() } returns true

        initSUT()
        sut.inputs.start()

        coVerify { mockConfigurationManager.applyPending() }
    }

    @Test
    fun `start activate then app shortcut manager disable interacted with`() = coroutineTest {

        every { mockConfigurationManager.requireSynchronisation } returns false
        coEvery { mockConfigurationManager.applyPending() } returns false

        initSUT()
        sut.inputs.start()

        coVerify { mockAppShortcutManager.disable() }
    }

    @Test
    fun `start activate go to next screen fired if activate fails`() = coroutineTest {

        every { mockConfigurationManager.requireSynchronisation } returns false
        coEvery { mockConfigurationManager.applyPending() } returns false

        initSUT()
        sut.inputs.start()

        sut.outputs.goToDashboard.test {
            assertEventFired()
        }
        sut.outputs.goToForceUpgrade.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `start activate go to force upgrade fired if activate fails and force upgrade exists`() = coroutineTest {

        every { mockConfigurationManager.forceUpgrade } returns ForceUpgrade("upgrade", "please", null)
        every { mockConfigurationManager.requireSynchronisation } returns false
        coEvery { mockConfigurationManager.applyPending() } returns false

        initSUT()
        sut.inputs.start()

        sut.outputs.goToDashboard.test {
            assertEventNotFired()
        }
        sut.outputs.goToForceUpgrade.test {
            assertEventFired()
        }
    }

    @Test
    fun `start activate go to next screen fired if activate passes`() = coroutineTest {

        every { mockConfigurationManager.requireSynchronisation } returns false
        coEvery { mockConfigurationManager.applyPending() } returns true

        initSUT()
        sut.inputs.start()

        sut.outputs.goToDashboard.test {
            assertEventFired()
        }
        sut.outputs.goToForceUpgrade.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `start activate go to force upgrade fired if activate passes`() {

        every { mockConfigurationManager.forceUpgrade } returns ForceUpgrade("upgrade", "please", null)
        every { mockConfigurationManager.requireSynchronisation } returns false
        coEvery { mockConfigurationManager.applyPending() } returns true

        initSUT()
        sut.inputs.start()

        sut.outputs.goToDashboard.test {
            assertEventNotFired()
        }
        sut.outputs.goToForceUpgrade.test {
            assertEventFired()
        }
    }
}