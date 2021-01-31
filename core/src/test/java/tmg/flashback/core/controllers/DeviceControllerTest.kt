package tmg.flashback.core.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.core.managers.BuildConfigManager
import tmg.flashback.core.repositories.CoreRepository

internal class DeviceControllerTest {

    private var mockDeviceRepository: CoreRepository = mockk(relaxed = true)
    private var mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)

    private lateinit var sut: DeviceController

    private fun initSUT() {
        sut = DeviceController(mockDeviceRepository, mockBuildConfigManager)
    }

    //region Shake to report

    @Test
    fun `shake to report reads from prefs`() {
        initSUT()
        every { mockDeviceRepository.shakeToReport } returns true
        assertTrue(mockDeviceRepository.shakeToReport)
        every { mockDeviceRepository.shakeToReport } returns false
        assertFalse(mockDeviceRepository.shakeToReport)

        verify(exactly = 2) {
            mockDeviceRepository.shakeToReport
        }
    }

    @Test
    fun `shake to report update saves to prefs`() {
        initSUT()
        sut.shakeToReport = true
        verify {
            mockDeviceRepository.shakeToReport = true
        }
    }

    //endregion

    //region First boot

    @Test
    fun `app opened count reads from prefs`() {
        every { mockDeviceRepository.appOpenedCount } returns 6
        initSUT()

        assertEquals(6, sut.appOpenedCount)
        verify {
            mockDeviceRepository.appOpenedCount
        }
    }

    @Test
    fun `app first boot reads from prefs`() {
        val expected = LocalDate.now().minusDays(2)
        every { mockDeviceRepository.appFirstBootTime } returns expected
        initSUT()

        assertEquals(expected, sut.appFirstBoot)
        verify {
            mockDeviceRepository.appFirstBootTime
        }
    }

    //endregion

    //region App opened

    @Test
    fun `app opened increments app count`() {
        every { mockDeviceRepository.appOpenedCount } returns 4
        every { mockBuildConfigManager.versionCode } returns 10
        initSUT()
        sut.appOpened()
        verify {
            mockDeviceRepository.appOpenedCount = 5
            mockDeviceRepository.lastAppVersion = 10
        }
    }

    //endregion

    //region Device UDID

    @Test
    fun `device udid is read from prefs`() {
        every { mockDeviceRepository.deviceUdid } returns "test"
        initSUT()

        assertEquals("test", sut.deviceUdid)
        verify {
            mockDeviceRepository.deviceUdid
        }
    }

    //endregion

    //region Last app version

    @Test
    fun `last app version is read from prefs`() {
        every { mockDeviceRepository.lastAppVersion } returns 3
        initSUT()

        assertEquals(3, sut.lastAppVersion)
        verify {
            mockDeviceRepository.lastAppVersion
        }
    }

    //endregion

}