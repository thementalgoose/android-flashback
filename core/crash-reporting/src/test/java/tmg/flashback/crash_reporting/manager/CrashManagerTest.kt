package tmg.flashback.crash_reporting.manager

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.crash_reporting.repository.CrashRepository
import tmg.flashback.crash_reporting.services.CrashService

internal class CrashManagerTest {

    private var mockCrashRepository: CrashRepository = mockk(relaxed = true)
    private var mockCrashService: CrashService = mockk(relaxed = true)

    private lateinit var underTest: CrashManager

    private fun initUnderTest() {
        underTest = CrashManager(mockCrashRepository, mockCrashService)
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