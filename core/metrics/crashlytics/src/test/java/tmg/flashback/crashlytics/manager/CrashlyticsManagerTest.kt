package tmg.flashback.crashlytics.manager

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.crashlytics.services.FirebaseCrashService
import tmg.flashback.device.repository.PrivacyRepository

internal class CrashlyticsManagerTest {

    private var mockPrivacyRepository: PrivacyRepository = mockk(relaxed = true)
    private var mockFirebaseCrashService: FirebaseCrashService = mockk(relaxed = true)

    private lateinit var underTest: CrashlyticsManager

    private fun initUnderTest() {
        underTest = CrashlyticsManager(mockPrivacyRepository, mockFirebaseCrashService)
    }

    //region Logging

    @Test
    fun `log msg forwards to firebase if toggle is enabled`() {
        every { mockPrivacyRepository.crashReporting } returns true

        initUnderTest()
        underTest.log("msg")

        verify { mockFirebaseCrashService.logInfo("msg") }
    }

    @Test
    fun `log msg forwards to firebase if toggle is disabled`() {
        every { mockPrivacyRepository.crashReporting } returns false

        initUnderTest()
        underTest.log("msg")

        verify(exactly = 0) { mockFirebaseCrashService.logInfo("msg") }
    }

    @Test
    fun `log error forwards to firebase if toggle is enabled`() {
        every { mockPrivacyRepository.crashReporting } returns true

        initUnderTest()
        underTest.logError("msg")

        verify { mockFirebaseCrashService.logError("msg") }
    }

    @Test
    fun `log error forwards to firebase if toggle is disabled`() {
        every { mockPrivacyRepository.crashReporting } returns false

        initUnderTest()
        underTest.logError("msg")

        verify(exactly = 0) { mockFirebaseCrashService.logError("msg") }
    }

    @Test
    fun `log msg with exception forwards to firebase if toggle is enabled`() {
        every { mockPrivacyRepository.crashReporting } returns true

        val exception = RuntimeException()
        initUnderTest()
        underTest.logException(exception, "msg")

        verify { mockFirebaseCrashService.logException(exception, "msg") }
    }

    @Test
    fun `log msg with exception forwards to firebase if toggle is disabled`() {
        every { mockPrivacyRepository.crashReporting } returns false

        val exception = RuntimeException()
        initUnderTest()
        underTest.logException(exception, "msg")

        verify(exactly = 0) { mockFirebaseCrashService.logException(exception, "msg") }
    }

    //endregion
}