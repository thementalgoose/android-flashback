package tmg.flashback

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.managers.AppShortcutManager
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.PrefDeviceRepository
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.assertEventNotFired
import tmg.flashback.testutils.test

class SplashViewModelTest: BaseTest() {

    private var mockAppShortcutManager: AppShortcutManager = mockk(relaxed = true)
    private var mockPrefDeviceRepository: PrefDeviceRepository = mockk(relaxed = true)
    private var mockRemoteConfigRepository: RemoteConfigRepository = mockk(relaxed = true)

    private lateinit var sut: SplashViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockAppShortcutManager.enable() } returns true
        every { mockAppShortcutManager.disable() } returns true
    }

    private fun initSUT() {
        sut = SplashViewModel(mockAppShortcutManager, mockPrefDeviceRepository, mockRemoteConfigRepository)
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

        every { mockPrefDeviceRepository.remoteConfigInitialSync } returns false
        coEvery { mockRemoteConfigRepository.update(any()) } returns true

        initSUT()
        sut.inputs.start()

        sut.outputs.showLoading.test {
            assertValue(true)
        }
    }

    @Test
    fun `SplashViewModel start fetch and activate disables show resync`() = coroutineTest {

        every { mockPrefDeviceRepository.remoteConfigInitialSync } returns false
        coEvery { mockRemoteConfigRepository.update(any()) } returns true

        initSUT()
        sut.inputs.start()

        sut.outputs.showResync.test {
            assertValue(false)
        }
    }

    @Test
    fun `SplashViewModel start fetch and activate calls remote config repo`() = coroutineTest {

        every { mockPrefDeviceRepository.remoteConfigInitialSync } returns false
        coEvery { mockRemoteConfigRepository.update(any()) } returns true

        initSUT()
        sut.inputs.start()

        coVerify { mockRemoteConfigRepository.update(true) }
    }

    @Test
    fun `SplashViewModel start fetch and activate when success sets initial remote initial config to true`() = coroutineTest {

        every { mockPrefDeviceRepository.remoteConfigInitialSync } returns false
        coEvery { mockRemoteConfigRepository.update(any()) } returns true

        initSUT()
        sut.inputs.start()

        verify { mockPrefDeviceRepository.remoteConfigInitialSync = true }
    }

    @Test
    fun `SplashViewModel start fetch and activate when success sets updates shortcut manager`() = coroutineTest {

        every { mockPrefDeviceRepository.remoteConfigInitialSync } returns false
        every { mockRemoteConfigRepository.rss } returns true
        coEvery { mockRemoteConfigRepository.update(any()) } returns true

        initSUT()
        sut.inputs.start()

        verify { mockAppShortcutManager.enable() }
    }

    @Test
    fun `SplashViewModel start fetch and activate when success it fires go to next screen`() = coroutineTest {

        every { mockPrefDeviceRepository.remoteConfigInitialSync } returns false
        coEvery { mockRemoteConfigRepository.update(any()) } returns true

        initSUT()
        sut.inputs.start()

        sut.outputs.goToNextScreen.test {
            assertEventFired()
        }
    }

    @Test
    fun `SplashViewModel start fetch and activate when failure then show resync sets to true`() = coroutineTest {

        every { mockPrefDeviceRepository.remoteConfigInitialSync } returns false
        coEvery { mockRemoteConfigRepository.update(any()) } returns false

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

        every { mockPrefDeviceRepository.remoteConfigInitialSync } returns false
        coEvery { mockRemoteConfigRepository.update(any()) } returns false

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

        every { mockPrefDeviceRepository.remoteConfigInitialSync } returns true
        coEvery { mockRemoteConfigRepository.activate() } returns true

        initSUT()
        sut.inputs.start()

        coVerify { mockRemoteConfigRepository.activate() }
    }

    @Test
    fun `SplashViewModel start activate then app shortcut manager disable interacted with`() = coroutineTest {

        every { mockPrefDeviceRepository.remoteConfigInitialSync } returns true
        coEvery { mockRemoteConfigRepository.activate() } returns false

        initSUT()
        sut.inputs.start()

        coVerify { mockAppShortcutManager.disable() }
    }

    @Test
    fun `SplashViewModel start activate go to next screen fired if activate fails`() = coroutineTest {

        every { mockPrefDeviceRepository.remoteConfigInitialSync } returns true
        coEvery { mockRemoteConfigRepository.activate() } returns false

        initSUT()
        sut.inputs.start()

        sut.outputs.goToNextScreen.test {
            assertEventFired()
        }
    }

    @Test
    fun `SplashViewModel start activate go to next screen fired if activate passes`() = coroutineTest {

        every { mockPrefDeviceRepository.remoteConfigInitialSync } returns true
        coEvery { mockRemoteConfigRepository.activate() } returns true

        initSUT()
        sut.inputs.start()

        sut.outputs.goToNextScreen.test {
            assertEventFired()
        }
    }
}