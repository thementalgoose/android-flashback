package tmg.flashback.ui.dashboard.expanded

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.releasenotes.usecases.NewReleaseNotesUseCase
import tmg.flashback.rss.RssNavigationComponent
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.ui.dashboard.expectedMenuItems
import tmg.flashback.ui.dashboard2.expanded.DashboardExpandedNavItem
import tmg.flashback.ui.dashboard2.expanded.DashboardExpandedScreenState
import tmg.flashback.ui.dashboard2.expanded.DashboardExpandedViewModel
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.usecases.DashboardSyncUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.assertEventNotFired
import tmg.testutils.livedata.test

internal class DashboardExpandedViewModelTest: BaseTest() {

    private val mockDashboardSyncUseCase: DashboardSyncUseCase = mockk(relaxed = true)
    private val mockGetSeasonsUseCase: GetSeasonsUseCase = mockk(relaxed = true)
    private val mockReleaseNotesUseCase: NewReleaseNotesUseCase = mockk(relaxed = true)
    private val mockDefaultSeasonUseCase: DefaultSeasonUseCase = mockk(relaxed = true)
    private val mockAnalyticsManager: AnalyticsManager = mockk(relaxed = true)
    private val mockNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)
    private val mockRssNavigationComponent: RssNavigationComponent = mockk(relaxed = true)
    private val mockStatsNavigationComponent: StatsNavigationComponent = mockk(relaxed = true)
    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)
    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)

    private lateinit var underTest: DashboardExpandedViewModel

    private fun initUnderTest() {
        underTest = DashboardExpandedViewModel(
            dashboardSyncUseCase = mockDashboardSyncUseCase,
            getSeasonsUseCase = mockGetSeasonsUseCase,
            releaseNotesUseCase = mockReleaseNotesUseCase,
            defaultSeasonUseCase = mockDefaultSeasonUseCase,
            analyticsManager = mockAnalyticsManager,
            navigationComponent = mockNavigationComponent,
            rssNavigationComponent = mockRssNavigationComponent,
            statsNavigationComponent = mockStatsNavigationComponent,
            raceRepository = mockRaceRepository,
            overviewRepository = mockOverviewRepository,
            ioDispatcher = Dispatchers.Unconfined,
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockDefaultSeasonUseCase.defaultSeason } returns 2019
    }

    @Test
    fun `default season data is fetched on initial load`() {
        every { mockDefaultSeasonUseCase.defaultSeason } returns 2020

        initUnderTest()

        coVerify {
            mockDashboardSyncUseCase.sync()
        }
        assertEquals(DashboardExpandedScreenState(DashboardExpandedNavItem.CALENDAR, 2020), underTest.initialTab)
    }

    //region Tabs

    @Test
    fun `current tab defaults to expected default tab`() {
        initUnderTest()

        underTest.outputs.currentTab.test {
            assertValue(
                DashboardExpandedScreenState(
                    tab = DashboardExpandedNavItem.CALENDAR,
                    season = 2019
                )
            )
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
    fun `clicking tab updates tab in current tab`() {
        initUnderTest()

        underTest.inputs.clickNavItem(DashboardExpandedNavItem.DRIVERS)
        underTest.outputs.currentTab.test {
            assertValue(DashboardExpandedScreenState(DashboardExpandedNavItem.DRIVERS, 2019))
        }
        mockAnalyticsManager.viewScreen("Dashboard", mapOf(
            "season" to "2019",
            "tab" to "Drivers"
        ))
    }

    @Test
    fun `clicking settings launches nav item`() {
        initUnderTest()
        underTest.inputs.clickNavItem(DashboardExpandedNavItem.SETTINGS)

        verify {
            mockNavigationComponent.settings()
        }
    }

    @Test
    fun `clicking rss launches nav item`() {
        initUnderTest()
        underTest.inputs.clickNavItem(DashboardExpandedNavItem.RSS)

        verify {
            mockRssNavigationComponent.rss()
        }
    }

    @Test
    fun `clicking search launches nav item`() {
        initUnderTest()
        underTest.inputs.clickNavItem(DashboardExpandedNavItem.SEARCH)

        verify {
            mockStatsNavigationComponent.search()
        }
    }

    @Test
    fun `clicking season launches fetch race request if season is default`() {
        initUnderTest()

        underTest.inputs.clickSeason(2019)
        underTest.outputs.currentTab.test {
            assertValue(DashboardExpandedScreenState(DashboardExpandedNavItem.CALENDAR, 2019))
        }
        coVerify {
            mockOverviewRepository.fetchOverview(2019)
            mockRaceRepository.fetchRaces(2019)
        }
    }

    @Test
    fun `clicking season updates season in current tab`() {
        initUnderTest()

        underTest.inputs.clickSeason(2018)
        underTest.outputs.currentTab.test {
            assertValue(DashboardExpandedScreenState(DashboardExpandedNavItem.CALENDAR, 2018))
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

        coEvery { mockDashboardSyncUseCase.sync() } returns false

        initUnderTest()
        advanceUntilIdle()

        underTest.outputs.appConfigSynced.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `init if update returns changes and activate successfully then notify app config synced event`() = coroutineTest {

        coEvery { mockDashboardSyncUseCase.sync() } returns true

        initUnderTest()
        advanceUntilIdle()

        underTest.outputs.appConfigSynced.test {
            assertEventFired()
        }
    }

    //endregion

    @Test
    fun `list of seasons returned`() {
        every { mockGetSeasonsUseCase.get(any()) } returns expectedMenuItems

        initUnderTest()
        underTest.seasons.test {
            assertValue(expectedMenuItems)
        }
    }

    //region Release notes

    @Test
    fun `init if release notes are pending then open release notes is fired`() {
        // Because notification onboarding takes priority over release notes
        every { mockReleaseNotesUseCase.getNotes() } returns listOf(mockk())
        initUnderTest()
        underTest.outputs.openReleaseNotes.test {
            assertEventFired()
        }
    }

    @Test
    fun `init if release notes are not pending then open release notes not fired`() {
        every { mockReleaseNotesUseCase.getNotes() } returns emptyList()
        initUnderTest()
        underTest.outputs.openReleaseNotes.test {
            assertEventNotFired()
        }
    }

    //endregion
}