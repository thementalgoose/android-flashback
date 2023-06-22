package tmg.flashback.crashlytics.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.crashlytics.services.CrashService
import tmg.flashback.device.repository.PrivacyRepository

internal class InitialiseCrashReportingUseCaseTest {

    private val mockPrivacyRepository: PrivacyRepository = mockk(relaxed = true)
    private val mockCrashService: CrashService = mockk(relaxed = true)

    private lateinit var underTest: InitialiseCrashReportingUseCase

    private fun initUnderTest() {
        underTest = InitialiseCrashReportingUseCase(
            mockPrivacyRepository,
            mockCrashService
        )
    }

    @Test
    fun `initialise sends metrics to crash service with feature enabled`() {
        val deviceUdid = "deviceUdid"
        val appOpenedCount = 1
        val appOpenedFirst = LocalDate.of(2021, 1, 1)
        every { mockPrivacyRepository.crashReporting } returns true

        initUnderTest()
        underTest.initialise(
            deviceUdid = deviceUdid,
            appOpenedCount = appOpenedCount,
            appFirstOpened = appOpenedFirst
        )

        verify {
            mockCrashService.initialise(
                enableCrashReporting = true,
                deviceUdid = deviceUdid,
                appFirstOpened = "01 Jan 2021",
                appOpenedCount = appOpenedCount
            )
        }
    }

    @Test
    fun `initialise sends metrics to crash service with feature disabled`() {
        val deviceUdid = "deviceUdid"
        val appOpenedCount = 1
        val appOpenedFirst = LocalDate.of(2021, 1, 1)
        every { mockPrivacyRepository.crashReporting } returns false

        initUnderTest()
        underTest.initialise(
            deviceUdid = deviceUdid,
            appOpenedCount = appOpenedCount,
            appFirstOpened = appOpenedFirst
        )

        verify {
            mockCrashService.initialise(
                enableCrashReporting = false,
                deviceUdid = deviceUdid,
                appFirstOpened = "01 Jan 2021",
                appOpenedCount = appOpenedCount
            )
        }
    }
}