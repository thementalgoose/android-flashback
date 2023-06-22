package tmg.flashback.crashlytics.manager

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.crashlytics.services.CrashService
import tmg.flashback.device.repository.PrivacyRepository

internal class CrashManagerTest {

    private var mockPrivacyRepository: PrivacyRepository = mockk(relaxed = true)
    private var mockCrashService: CrashService = mockk(relaxed = true)

    private lateinit var underTest: CrashManager

    private fun initUnderTest() {
        underTest = CrashManager(mockPrivacyRepository, mockCrashService)
    }

    //region Logging

    @Test
    fun `log msg forwards to firebase if toggle is enabled`() {
        every { mockPrivacyRepository.crashReporting } returns true

        initUnderTest()
        underTest.log("msg")

        verify { mockCrashService.logInfo("msg") }
    }

    @Test
    fun `log msg forwards to firebase if toggle is disabled`() {
        every { mockPrivacyRepository.crashReporting } returns false

        initUnderTest()
        underTest.log("msg")

        verify(exactly = 0) { mockCrashService.logInfo("msg") }
    }

    @Test
    fun `log error forwards to firebase if toggle is enabled`() {
        every { mockPrivacyRepository.crashReporting } returns true

        initUnderTest()
        underTest.logError("msg")

        verify { mockCrashService.logError("msg") }
    }

    @Test
    fun `log error forwards to firebase if toggle is disabled`() {
        every { mockPrivacyRepository.crashReporting } returns false

        initUnderTest()
        underTest.logError("msg")

        verify(exactly = 0) { mockCrashService.logError("msg") }
    }

    @Test
    fun `log msg with exception forwards to firebase if toggle is enabled`() {
        every { mockPrivacyRepository.crashReporting } returns true

        val exception = RuntimeException()
        initUnderTest()
        underTest.logException(exception, "msg")

        verify { mockCrashService.logException(exception, "msg") }
    }

    @Test
    fun `log msg with exception forwards to firebase if toggle is disabled`() {
        every { mockPrivacyRepository.crashReporting } returns false

        val exception = RuntimeException()
        initUnderTest()
        underTest.logException(exception, "msg")

        verify(exactly = 0) { mockCrashService.logException(exception, "msg") }
    }

    //endregion
}