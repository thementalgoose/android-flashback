package tmg.flashback.crash_reporting.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.crash_reporting.repository.CrashRepository
import tmg.flashback.crash_reporting.services.CrashService

internal class InitialiseCrashReportingUseCaseTest {

    private val mockCrashRepository: CrashRepository = mockk(relaxed = true)
    private val mockCrashService: CrashService = mockk(relaxed = true)

    private lateinit var underTest: InitialiseCrashReportingUseCase

    private fun initUnderTest() {
        underTest = InitialiseCrashReportingUseCase(
            mockCrashRepository,
            mockCrashService
        )
    }

    @Test
    fun `initialise sends metrics to crash service with feature enabled`() {
        val deviceUdid = "deviceUdid"
        val appOpenedCount = 1
        val appOpenedFirst = LocalDate.of(2021, 1, 1)
        every { mockCrashRepository.isEnabled } returns true

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
        every { mockCrashRepository.isEnabled } returns false

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