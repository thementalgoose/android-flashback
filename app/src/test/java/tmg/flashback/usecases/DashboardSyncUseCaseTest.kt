package tmg.flashback.usecases

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.results.usecases.DefaultSeasonUseCase
import tmg.flashback.results.usecases.ScheduleNotificationsUseCase
import tmg.flashback.widgets.usecases.UpdateWidgetsUseCaseImpl

internal class DashboardSyncUseCaseTest {

    private val mockUpdateWidgetsUseCase: UpdateWidgetsUseCaseImpl = mockk(relaxed = true)
    private val mockScheduleNotificationsUseCase: ScheduleNotificationsUseCase = mockk(relaxed = true)
    private val mockDefaultSeasonUseCase: DefaultSeasonUseCase = mockk(relaxed = true)
    private val mockFetchConfigUseCase: FetchConfigUseCase = mockk(relaxed = true)
    private val mockApplyConfigUseCase: ApplyConfigUseCase = mockk(relaxed = true)
    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)
    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)

    private lateinit var underTest: DashboardSyncUseCase

    private fun initUnderTest() {
        underTest = DashboardSyncUseCase(
            updateWidgetsUseCase = mockUpdateWidgetsUseCase,
            scheduleNotificationsUseCase = mockScheduleNotificationsUseCase,
            defaultSeasonUseCase = mockDefaultSeasonUseCase,
            fetchConfigUseCase = mockFetchConfigUseCase,
            applyConfigUseCase = mockApplyConfigUseCase,
            raceRepository = mockRaceRepository,
            overviewRepository = mockOverviewRepository,
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockDefaultSeasonUseCase.defaultSeason } returns 2020
    }

    @Test
    fun `sync fetches and activates config`() {
        coEvery { mockApplyConfigUseCase.apply() } returns false

        initUnderTest()
        runBlocking { underTest.sync() }

        coVerify {
            mockFetchConfigUseCase.fetch()
            mockApplyConfigUseCase.apply()
        }
        verify(exactly = 0) {
            mockScheduleNotificationsUseCase.schedule()
            mockUpdateWidgetsUseCase.update()
        }
    }

    @Test
    fun `sync scheduled notifications if fetches and activates config successful`() {
        coEvery { mockApplyConfigUseCase.apply() } returns true

        initUnderTest()
        runBlocking { underTest.sync() }

        coVerify {
            mockFetchConfigUseCase.fetch()
            mockApplyConfigUseCase.apply()
        }
        verify {
            mockScheduleNotificationsUseCase.schedule()
            mockUpdateWidgetsUseCase.update()
        }
    }

    @Test
    fun `sync fetches default season races`() {
        initUnderTest()
        runBlocking { underTest.sync() }

        coVerify { mockRaceRepository.fetchRaces(2020) }
    }

    @Test
    fun `sync fetches default season overview`() {
        initUnderTest()
        runBlocking { underTest.sync() }

        coVerify { mockOverviewRepository.fetchOverview(2020) }
    }
}