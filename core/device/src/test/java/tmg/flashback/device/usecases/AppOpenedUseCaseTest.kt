package tmg.flashback.device.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.DeviceRepository

internal class AppOpenedUseCaseTest {

    private val mockDeviceRepository: DeviceRepository = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)

    private lateinit var underTest: AppOpenedUseCase

    private fun initUnderTest() {
        underTest = AppOpenedUseCase(
            mockDeviceRepository,
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
}