package tmg.crash_reporting.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.crash_reporting.services.CrashService
import tmg.crash_reporting.repository.CrashRepository
import java.lang.RuntimeException

internal class CrashControllerTest {

    private var mockCrashRepository: CrashRepository = mockk(relaxed = true)
    private var mockCrashService: CrashService = mockk(relaxed = true)

    private lateinit var sut: CrashController

    private fun initSUT() {
        sut = CrashController(mockCrashRepository, mockCrashService)
    }

    //region Crash reporting enabled

    @Test
    fun `crash reporting enabled`() {
        every { mockCrashRepository.isEnabled } returns true
        initSUT()

        Assertions.assertTrue(sut.enabled)
        verify { mockCrashRepository.isEnabled }
    }

    @Test
    fun `crash reporting disabled`() {
        every { mockCrashRepository.isEnabled } returns false
        initSUT()

        Assertions.assertFalse(sut.enabled)
        verify { mockCrashRepository.isEnabled }
    }

    @Test
    fun `crash reporting update saves in prefs`() {

        initSUT()

        sut.enabled = true
        verify { mockCrashRepository.isEnabled = true }
    }

    //endregion

    //region Initialisation

    @Test
    fun `initialise sends all data to firebase`() {
        val now = LocalDate.now()
        val expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        every { mockCrashRepository.isEnabled } returns true

        initSUT()
        sut.initialise(
            deviceUdid = "test-udid",
            appOpenedCount = 1,
            appFirstOpened = now
        )

        verify {
            mockCrashService.initialise(true,  "test-udid", expectedDate, 1)
        }
    }

    //endregion

    //region Logging

    @Test
    fun `log msg forwards to firebase if toggle is enabled`() {
        every { mockCrashRepository.isEnabled } returns true

        initSUT()
        sut.log("msg")

        verify { mockCrashService.logInfo("msg") }
    }

    @Test
    fun `log msg forwards to firebase if toggle is disabled`() {
        every { mockCrashRepository.isEnabled } returns false

        initSUT()
        sut.log("msg")

        verify(exactly = 0) { mockCrashService.logInfo("msg") }
    }

    @Test
    fun `log error forwards to firebase if toggle is enabled`() {
        every { mockCrashRepository.isEnabled } returns true

        initSUT()
        sut.logError("msg")

        verify { mockCrashService.logError("msg") }
    }

    @Test
    fun `log error forwards to firebase if toggle is disabled`() {
        every { mockCrashRepository.isEnabled } returns false

        initSUT()
        sut.logError("msg")

        verify(exactly = 0) { mockCrashService.logError("msg") }
    }

    @Test
    fun `log msg with exception forwards to firebase if toggle is enabled`() {
        every { mockCrashRepository.isEnabled } returns true

        val exception = RuntimeException()
        initSUT()
        sut.logError(exception, "msg")

        verify { mockCrashService.logException(exception, "msg") }
    }

    @Test
    fun `log msg with exception forwards to firebase if toggle is disabled`() {
        every { mockCrashRepository.isEnabled } returns false

        val exception = RuntimeException()
        initSUT()
        sut.logError(exception, "msg")

        verify(exactly = 0) { mockCrashService.logException(exception, "msg") }
    }

    //endregion
}