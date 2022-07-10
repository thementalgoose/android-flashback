package tmg.flashback.ui.dashboard

import android.content.Context
import io.mockk.*
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.analytics.di.analyticsModule
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.releasenotes.usecases.NewReleaseNotesUseCase
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.statistics.workmanager.WorkerProvider
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.stats.usecases.ScheduleNotificationsUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.assertEventNotFired
import tmg.testutils.livedata.test

internal class DashboardViewModelTest: BaseTest() {

    lateinit var underTest: DashboardViewModel

    private val mockContext: Context = mockk(relaxed = true)
    private val mockScheduleNotificationsUseCase: ScheduleNotificationsUseCase = mockk(relaxed = true)
    private val mockDefaultSeasonUseCase: DefaultSeasonUseCase = mockk(relaxed = true)
    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)
    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private val mockApplyConfigUseCase: ApplyConfigUseCase = mockk(relaxed = true)
    private val mockFetchConfigUseCase: FetchConfigUseCase = mockk(relaxed = true)
    private val mockAnalyticsManager: AnalyticsManager = mockk(relaxed = true)
    private val mockNewReleaseNotesUseCase: NewReleaseNotesUseCase = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {
        coEvery { mockApplyConfigUseCase.apply() } returns false
        every { mockNewReleaseNotesUseCase.getNotes() } returns emptyList()
        every { mockDefaultSeasonUseCase.defaultSeason } returns 2019
    }

    private fun initUnderTest() {
        underTest = DashboardViewModel(
            mockContext,
            mockScheduleNotificationsUseCase,
            mockDefaultSeasonUseCase,
            mockFetchConfigUseCase,
            mockApplyConfigUseCase,
            mockRaceRepository,
            mockOverviewRepository,
            mockNewReleaseNotesUseCase,
            mockAnalyticsManager,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @Test
    fun `default season data is fetched on initial load`() {
        every { mockDefaultSeasonUseCase.defaultSeason } returns 2020

        initUnderTest()

        coVerify {
            mockRaceRepository.fetchRaces(2020)
            mockOverviewRepository.fetchOverview(2020)
        }
    }

    //region Tabs

    @Test
    fun `current tab defaults to expected default tab`() {
        initUnderTest()

        underTest.outputs.currentTab.test {
            assertValue(DashboardScreenState(
                tab = DashboardNavItem.CALENDAR,
                season = 2019
            ))
        }
        verify {
            mockAnalyticsManager.viewScreen("Dashboard", mapOf(
                "season" to "2019",
                "tab" to "Calendar"
            ))
            mockDefaultSeasonUseCase.defaultSeason
        }
    }

    @Test
    fun `clicking season updates season in current tab`() {
        initUnderTest()

        underTest.inputs.clickTab(DashboardNavItem.DRIVERS)
        underTest.outputs.currentTab.test {
            assertValue(DashboardScreenState(DashboardNavItem.DRIVERS, 2019))
        }
        mockAnalyticsManager.viewScreen("Dashboard", mapOf(
            "season" to "2019",
            "tab" to "Drivers"
        ))
    }

    @Test
    fun `clicking season launches fetch race request if season is default`() {
        initUnderTest()

        underTest.inputs.clickTab(DashboardNavItem.DRIVERS)
        underTest.outputs.currentTab.test {
            assertValue(DashboardScreenState(DashboardNavItem.DRIVERS, 2019))
        }
        coVerify {
            mockOverviewRepository.fetchOverview(2019)
            mockRaceRepository.fetchRaces(2019)
        }
    }

    @Test
    fun `clicking tab updates tab in current tab`() {
        initUnderTest()

        underTest.inputs.clickSeason(2018)
        underTest.outputs.currentTab.test {
            assertValue(DashboardScreenState(DashboardNavItem.CALENDAR, 2018))
        }
        verify {
            mockAnalyticsManager.viewScreen("Dashboard", mapOf(
                "season" to "2018",
                "tab" to "Calendar"
            ))
        }
    }

    //endregion

    //region Remote config fetch and sync

    @Test
    fun `init if update returns changes and activate fails nothing happens`() = coroutineTest {

        coEvery { mockApplyConfigUseCase.apply() } returns false

        initUnderTest()
        advanceUntilIdle()

        underTest.outputs.appConfigSynced.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `init if update returns changes and activate successfully then notify app config synced event`() = coroutineTest {

        coEvery { mockApplyConfigUseCase.apply() } returns true

        initUnderTest()
        advanceUntilIdle()

        underTest.outputs.appConfigSynced.test {
            assertEventFired()
        }
        coVerify {
            mockScheduleNotificationsUseCase.schedule()
        }
    }

    //endregion

    //region Release notes

    @Test
    fun `init if release notes are pending then open release notes is fired`() {
        // Because notification onboarding takes priority over release notes
        every { mockNewReleaseNotesUseCase.getNotes() } returns listOf(mockk())
        initUnderTest()
        underTest.outputs.openReleaseNotes.test {
            assertEventFired()
        }
    }

    @Test
    fun `init if release notes are not pending then open release notes not fired`() {
        every { mockNewReleaseNotesUseCase.getNotes() } returns emptyList()
        initUnderTest()
        underTest.outputs.openReleaseNotes.test {
            assertEventNotFired()
        }
    }

    //endregion
}