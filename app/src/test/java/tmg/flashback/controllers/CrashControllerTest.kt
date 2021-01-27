package tmg.flashback.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.firebase.crash.FirebaseCrashManager
import tmg.flashback.repo.pref.DeviceRepository
import tmg.flashback.testutils.BaseTest
import java.lang.RuntimeException

internal class CrashControllerTest: BaseTest() {

    private var mockDeviceRepository: DeviceRepository = mockk(relaxed = true)
    private var mockFirebaseCrashReportManager: FirebaseCrashManager = mockk(relaxed = true)

    private lateinit var sut: CrashController

    private fun initSUT() {
        sut = CrashController(mockDeviceRepository, mockFirebaseCrashReportManager)
    }

    //region Crash reporting enabled

    @Test
    fun `CrashController crash reporting enabled`() {
        every { mockDeviceRepository.crashReporting } returns true
        initSUT()

        assertTrue(sut.crashReporting)
        verify { mockDeviceRepository.crashReporting }
    }

    @Test
    fun `CrashController crash reporting disabled`() {
        every { mockDeviceRepository.crashReporting } returns false
        initSUT()

        assertFalse(sut.crashReporting)
        verify { mockDeviceRepository.crashReporting }
    }

    @Test
    fun `CrashController crash reporting update saves in prefs`() {

        initSUT()

        sut.crashReporting = true
        verify { mockDeviceRepository.crashReporting = true }
    }

    //endregion

    //region Initialisation

    @Test
    fun `CrashController initialise sends all data to firebase`() {
        val expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        every { mockDeviceRepository.appFirstBootTime } returns LocalDate.now()
        every { mockDeviceRepository.optInAnalytics } returns true
        every { mockDeviceRepository.appOpenedCount } returns 1
        every { mockDeviceRepository.deviceUdid } returns "test-udid"
        every { mockDeviceRepository.crashReporting } returns true

        initSUT()
        sut.initialiseCrashReporting()

        verify {
            mockFirebaseCrashReportManager.initialise(true, true, "test-udid", expectedDate, 1)
        }
    }

    //endregion

    //region Logging

    @Test
    fun `CrashController log msg forwards to firebase`() {
        initSUT()
        sut.log("msg")

        verify { mockFirebaseCrashReportManager.logError("msg") }
    }

    @Test
    fun `CrashController log msg with exception forwards to firebase`() {
        val exception = RuntimeException()
        initSUT()
        sut.logError(exception, "msg")

        verify { mockFirebaseCrashReportManager.logException(exception, "msg") }
    }

    //endregion
}