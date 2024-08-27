package tmg.flashback.crashlytics.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.crashlytics.model.FirebaseKey
import tmg.flashback.crashlytics.services.FirebaseCrashService
import tmg.flashback.device.repository.PrivacyRepository

internal class InitialiseCrashReportingUseCaseTest {

    private val mockPrivacyRepository: PrivacyRepository = mockk(relaxed = true)
    private val mockFirebaseCrashService: FirebaseCrashService = mockk(relaxed = true)

    private lateinit var underTest: InitialiseCrashReportingUseCase

    private fun initUnderTest() {
        underTest = InitialiseCrashReportingUseCase(
            mockPrivacyRepository,
            mockFirebaseCrashService
        )
    }

    @Test
    fun `initialise sends metrics to crash service with feature enabled`() {
        val deviceUuid = "deviceUuid"
        val keys = mapOf(FirebaseKey.AppFirstOpen to "X")
        every { mockPrivacyRepository.crashReporting } returns true

        initUnderTest()
        underTest.initialise(
            deviceUuid = deviceUuid,
            extraKeys = keys,
        )

        verify {
            mockFirebaseCrashService.initialise(
                enableCrashReporting = true,
                deviceUuid = deviceUuid,
                extraKeys = keys
            )
        }
    }

    @Test
    fun `initialise sends metrics to crash service with feature disabled`() {
        val deviceUuid = "deviceUuid"
        val keys = mapOf(FirebaseKey.AppFirstOpen to "X")
        every { mockPrivacyRepository.crashReporting } returns false

        initUnderTest()
        underTest.initialise(
            deviceUuid = deviceUuid,
            extraKeys = keys,
        )

        verify {
            mockFirebaseCrashService.initialise(
                enableCrashReporting = false,
                deviceUuid = deviceUuid,
                extraKeys = keys
            )
        }
    }
}