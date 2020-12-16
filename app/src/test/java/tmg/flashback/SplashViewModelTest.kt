package tmg.flashback

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import tmg.flashback.managers.AppShortcutManager
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.PrefDeviceRepository
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.assertEventNotFired
import tmg.flashback.testutils.test

class SplashViewModelTest: BaseTest() {

    private var mockAppShortcutManager: AppShortcutManager = mock()
    private var mockPrefDeviceRepository: PrefDeviceRepository = mock()
    private var mockRemoteConfigRepository: RemoteConfigRepository = mock()

    private lateinit var sut: SplashViewModel

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

        whenever(mockPrefDeviceRepository.remoteConfigInitialSync).thenReturn(false)
        mockRemoteConfigRepository.stub {
            onBlocking { update(true) } doReturn true
        }

        initSUT()
        sut.inputs.start()

        sut.outputs.showLoading.test {
            assertValue(true)
        }
    }

    @Test
    fun `SplashViewModel start fetch and activate disables show resync`() = coroutineTest {

        whenever(mockPrefDeviceRepository.remoteConfigInitialSync).thenReturn(false)
        mockRemoteConfigRepository.stub {
            onBlocking { update(true) } doReturn true
        }

        initSUT()
        sut.inputs.start()

        sut.outputs.showResync.test {
            assertValue(false)
        }
    }

    @Test
    fun `SplashViewModel start fetch and activate calls remote config repo`() = coroutineTest {

        whenever(mockPrefDeviceRepository.remoteConfigInitialSync).thenReturn(false)
        mockRemoteConfigRepository.stub {
            onBlocking { update(true) } doReturn true
        }

        initSUT()
        sut.inputs.start()

        runBlockingTest {
            verify(mockRemoteConfigRepository).update(true)
        }
    }

    @Test
    fun `SplashViewModel start fetch and activate when success sets initial remote initial config to true`() = coroutineTest {

        whenever(mockPrefDeviceRepository.remoteConfigInitialSync).thenReturn(false)
        mockRemoteConfigRepository.stub {
            onBlocking { update(true) } doReturn true
        }

        initSUT()
        sut.inputs.start()

        verify(mockPrefDeviceRepository).remoteConfigInitialSync = true
    }

    @Test
    fun `SplashViewModel start fetch and activate when success sets updates shortcut manager`() = coroutineTest {

        whenever(mockPrefDeviceRepository.remoteConfigInitialSync).thenReturn(false)
        mockRemoteConfigRepository.stub {
            onBlocking { update(true) } doReturn true
        }

        initSUT()
        sut.inputs.start()

        verify(mockAppShortcutManager).enable()
    }

    @Test
    fun `SplashViewModel start fetch and activate when success it fires go to next screen`() = coroutineTest {

        whenever(mockPrefDeviceRepository.remoteConfigInitialSync).thenReturn(false)
        mockRemoteConfigRepository.stub {
            onBlocking { update(true) } doReturn true
        }

        initSUT()
        sut.inputs.start()

        sut.outputs.goToNextScreen.test {
            assertEventFired()
        }
    }

    @Test
    fun `SplashViewModel start fetch and activate when failure then show resync sets to true`() = coroutineTest {

        whenever(mockPrefDeviceRepository.remoteConfigInitialSync).thenReturn(false)
        mockRemoteConfigRepository.stub {
            onBlocking { update(true) } doReturn false
        }

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
    fun `SplashViewModel start fetch and activate when failure then show loading sets to false`() = coroutineTest {

        whenever(mockPrefDeviceRepository.remoteConfigInitialSync).thenReturn(false)
        whenever(mockRemoteConfigRepository.rss).thenReturn(true)
        mockRemoteConfigRepository.stub {
            onBlocking { update(true) } doReturn false
        }

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

        whenever(mockPrefDeviceRepository.remoteConfigInitialSync).thenReturn(true)
        mockRemoteConfigRepository.stub {
            onBlocking { activate() } doReturn true
        }

        initSUT()
        sut.inputs.start()

        runBlockingTest {
            verify(mockRemoteConfigRepository).activate()
        }
    }

    @Test
    fun `SplashViewModel start activate then app shortcut manager disable interacted with`() = coroutineTest {

        whenever(mockPrefDeviceRepository.remoteConfigInitialSync).thenReturn(true)
        mockRemoteConfigRepository.stub {
            onBlocking { activate() } doReturn false
        }

        initSUT()
        sut.inputs.start()

        verify(mockAppShortcutManager).disable()
    }

    @Test
    fun `SplashViewModel start activate go to next screen fired if activate fails`() = coroutineTest {

        whenever(mockPrefDeviceRepository.remoteConfigInitialSync).thenReturn(true)
        mockRemoteConfigRepository.stub {
            onBlocking { activate() } doReturn false
        }

        initSUT()
        sut.inputs.start()

        sut.outputs.goToNextScreen.test {
            assertEventFired()
        }
    }

    @Test
    fun `SplashViewModel start activate go to next screen fired if activate passes`() = coroutineTest {

        whenever(mockPrefDeviceRepository.remoteConfigInitialSync).thenReturn(true)
        mockRemoteConfigRepository.stub {
            onBlocking { activate() } doReturn true
        }

        initSUT()
        sut.inputs.start()

        sut.outputs.goToNextScreen.test {
            assertEventFired()
        }
    }
}