package tmg.flashback.device.usecases

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.managers.FirebaseInstallationManager
import tmg.flashback.device.repository.DeviceRepository

internal class AppOpenedUseCaseTest {

    private val mockDeviceRepository: DeviceRepository = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)
    private val mockFirebaseInstallationManager: FirebaseInstallationManager = mockk(relaxed = true)

    private lateinit var underTest: AppOpenedUseCase

    private fun initUnderTest() {
        underTest = AppOpenedUseCase(
            mockDeviceRepository,
            mockFirebaseInstallationManager,
            mockBuildConfigManager
        )
    }

    @Test
    fun `app opened for first time increments counter and saves latest version`() {
        every { mockDeviceRepository.appOpenedCount } returns 0
        every { mockBuildConfigManager.versionCode } returns 3

        initUnderTest()
        underTest.run()

        val date = slot<LocalDate>()
        verify {
            mockDeviceRepository.appFirstOpened = capture(date)
            mockDeviceRepository.appOpenedCount = 1
            mockDeviceRepository.lastAppVersion = 3
        }
        assertEquals(LocalDate.now(), date.captured)
    }

    @Test
    fun `app opened for second time increments counter and saves latest version`() {
        every { mockDeviceRepository.appOpenedCount } returns 1
        every { mockBuildConfigManager.versionCode } returns 3

        initUnderTest()
        underTest.run()

        verify(exactly = 0) {
            mockDeviceRepository.appFirstOpened = any()
        }
        verify {
            mockDeviceRepository.appOpenedCount = 2
            mockDeviceRepository.lastAppVersion = 3
        }
    }

    @Test
    fun `app opened preload preloads installation id from firebase and saves to repo`() {
        val value = "installation-id"
        coEvery { mockFirebaseInstallationManager.getInstallationId() } returns value

        initUnderTest()
        runBlocking {
            underTest.preload()
        }

        coVerify {
            mockFirebaseInstallationManager.getInstallationId()
            mockDeviceRepository.installationId = value
        }
    }
}