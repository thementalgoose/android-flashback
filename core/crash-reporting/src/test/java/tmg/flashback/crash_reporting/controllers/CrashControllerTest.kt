package tmg.flashback.crash_reporting.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.crash_reporting.repository.CrashRepository
import tmg.flashback.crash_reporting.services.CrashService

internal class CrashControllerTest {

    private var mockCrashRepository: CrashRepository = mockk(relaxed = true)
    private var mockCrashService: CrashService = mockk(relaxed = true)

    private lateinit var underTest: CrashController

    private fun initUnderTest() {
        underTest = CrashController(mockCrashRepository, mockCrashService)
    }

    //region Logging

    @Test
    fun `log msg forwards to firebase if toggle is enabled`() {
        every { mockCrashRepository.isEnabled } returns true

        initUnderTest()
        underTest.log("msg")

        verify { mockCrashService.logInfo("msg") }
    }

    @Test
    fun `log msg forwards to firebase if toggle is disabled`() {
        every { mockCrashRepository.isEnabled } returns false

        initUnderTest()
        underTest.log("msg")

        verify(exactly = 0) { mockCrashService.logInfo("msg") }
    }

    @Test
    fun `log error forwards to firebase if toggle is enabled`() {
        every { mockCrashRepository.isEnabled } returns true

        initUnderTest()
        underTest.logError("msg")

        verify { mockCrashService.logError("msg") }
    }

    @Test
    fun `log error forwards to firebase if toggle is disabled`() {
        every { mockCrashRepository.isEnabled } returns false

        initUnderTest()
        underTest.logError("msg")

        verify(exactly = 0) { mockCrashService.logError("msg") }
    }

    @Test
    fun `log msg with exception forwards to firebase if toggle is enabled`() {
        every { mockCrashRepository.isEnabled } returns true

        val exception = RuntimeException()
        initUnderTest()
        underTest.logException(exception, "msg")

        verify { mockCrashService.logException(exception, "msg") }
    }

    @Test
    fun `log msg with exception forwards to firebase if toggle is disabled`() {
        every { mockCrashRepository.isEnabled } returns false

        val exception = RuntimeException()
        initUnderTest()
        underTest.logException(exception, "msg")

        verify(exactly = 0) { mockCrashService.logException(exception, "msg") }
    }

    //endregion
}