package tmg.flashback.ui

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.controllers.FeatureController
import tmg.flashback.managers.appshortcuts.AppShortcutManager
import tmg.flashback.managers.remoteconfig.RemoteConfigManager
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.DeviceRepository
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.assertEventNotFired
import tmg.flashback.testutils.test

internal class SplashViewModelTest: BaseTest() {

    private var mockAppShortcutManager: AppShortcutManager = mockk(relaxed = true)
    private var mockFeatureController: FeatureController = mockk(relaxed = true)
    private var mockRemoteConfigManager: RemoteConfigManager = mockk(relaxed = true)

    private lateinit var sut: SplashViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockAppShortcutManager.enable() } returns true
        every { mockAppShortcutManager.disable() } returns true
    }

    private fun initSUT() {
        sut = SplashViewModel(mockAppShortcutManager, mockFeatureController, mockRemoteConfigManager)
    }

    @Test
    fun `SplashViewModel show resync is default to false and show loading default to false`() = coroutineTest {

        initSUT()

        sut.outputs.showLoading.test {
            assertValue(false)
        }
        sut.outputs.showResync.test {
            assertValue(false)
        }
    }

    @Test
    fun `SplashViewModel start fetch and activate enables show loading`() = coroutineTest {

        every { mockRemoteConfigManager.requiresRemoteSync } returns true
        coEvery { mockRemoteConfigManager.update(any()) } returns true

        initSUT()
        sut.inputs.start()

        sut.outputs.showLoading.test {
            assertValue(true)
        }
    }

    @Test
    fun `SplashViewModel start fetch and activate disables show resync`() = coroutineTest {

        every { mockRemoteConfigManager.requiresRemoteSync } returns true
        coEvery { mockRemoteConfigManager.update(any()) } returns true

        initSUT()
        sut.inputs.start()

        sut.outputs.showResync.test {
            assertValue(false)
        }
    }

    @Test
    fun `SplashViewModel start fetch and activate calls remote config repo`() = coroutineTest {

        every { mockRemoteConfigManager.requiresRemoteSync } returns true
        coEvery { mockRemoteConfigManager.update(any()) } returns true

        initSUT()
        sut.inputs.start()

        coVerify { mockRemoteConfigManager.update(true) }
    }

    @Test
    fun `SplashViewModel start fetch and activate when success sets updates shortcut manager`() = coroutineTest {

        every { mockRemoteConfigManager.requiresRemoteSync } returns true
        every { mockFeatureController.rssEnabled } returns true
        coEvery { mockRemoteConfigManager.update(any()) } returns true

        initSUT()
        sut.inputs.start()

        verify { mockAppShortcutManager.enable() }
    }

    @Test
    fun `SplashViewModel start fetch and activate when success it fires go to next screen`() = coroutineTest {

        every { mockRemoteConfigManager.requiresRemoteSync } returns true
        coEvery { mockRemoteConfigManager.update(any()) } returns true

        initSUT()
        sut.inputs.start()

        sut.outputs.goToNextScreen.test {
            assertEventFired()
        }
    }

    @Test
    fun `SplashViewModel start fetch and activate when failure then show resync sets to true`() = coroutineTest {

        every { mockRemoteConfigManager.requiresRemoteSync } returns true
        coEvery { mockRemoteConfigManager.update(any()) } returns false

        initSUT()
        sut.inputs.start()

        sut.outputs.showResync.test {
            assertValue(true)
        }
        sut.outputs.goToNextScreen.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `SplashViewModel start fetch and activate when failure then show loading is set to false`() = coroutineTest {

        every { mockRemoteConfigManager.requiresRemoteSync } returns true
        coEvery { mockRemoteConfigManager.update(any()) } returns false

        initSUT()
        sut.inputs.start()

        sut.outputs.showLoading.test {
            assertValue(false)
        }
        sut.outputs.goToNextScreen.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `SplashViewModel start activate then remote config activate called`() = coroutineTest {

        every { mockRemoteConfigManager.requiresRemoteSync } returns false
        coEvery { mockRemoteConfigManager.activate() } returns true

        initSUT()
        sut.inputs.start()

        coVerify { mockRemoteConfigManager.activate() }
    }

    @Test
    fun `SplashViewModel start activate then app shortcut manager disable interacted with`() = coroutineTest {

        every { mockRemoteConfigManager.requiresRemoteSync } returns false
        coEvery { mockRemoteConfigManager.activate() } returns false

        initSUT()
        sut.inputs.start()

        coVerify { mockAppShortcutManager.disable() }
    }

    @Test
    fun `SplashViewModel start activate go to next screen fired if activate fails`() = coroutineTest {

        every { mockRemoteConfigManager.requiresRemoteSync } returns false
        coEvery { mockRemoteConfigManager.activate() } returns false

        initSUT()
        sut.inputs.start()

        sut.outputs.goToNextScreen.test {
            assertEventFired()
        }
    }

    @Test
    fun `SplashViewModel start activate go to next screen fired if activate passes`() = coroutineTest {

        every { mockRemoteConfigManager.requiresRemoteSync } returns false
        coEvery { mockRemoteConfigManager.activate() } returns true

        initSUT()
        sut.inputs.start()

        sut.outputs.goToNextScreen.test {
            assertEventFired()
        }
    }
}