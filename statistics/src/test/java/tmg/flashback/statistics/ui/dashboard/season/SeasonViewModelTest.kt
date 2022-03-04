package tmg.flashback.statistics.ui.dashboard.season

import io.mockk.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.Year
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.R
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.repo.EventsRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.statistics.repo.SeasonRepository
import tmg.flashback.statistics.repo.repository.CacheRepository
import tmg.flashback.statistics.repository.models.Banner
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.flashback.ui.model.AnimationSpeed
import tmg.flashback.ui.repository.ThemeRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class SeasonViewModelTest: BaseTest() {

    private val mockHomeController: HomeController = mockk(relaxed = true)
    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)
    private val mockEventsRepository: EventsRepository = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private val mockSeasonRepository: SeasonRepository = mockk(relaxed = true)
    private val mockAnalyticsManager: AnalyticsManager = mockk(relaxed = true)
    private val mockThemeRepository: ThemeRepository = mockk(relaxed = true)
    private val mockCacheRepository: CacheRepository = mockk(relaxed = true)

    private lateinit var sut: SeasonViewModel

    private fun initSUT() {
        sut = SeasonViewModel(
            mockHomeController,
            mockRaceRepository,
            mockEventsRepository,
            mockNetworkConnectivityManager,
            mockOverviewRepository,
            mockSeasonRepository,
            mockAnalyticsManager,
            mockThemeRepository,
            mockCacheRepository,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockCacheRepository.shouldSyncCurrentSeason() } returns false
        every { mockNetworkConnectivityManager.isConnected } returns true
        every { mockHomeController.banner } returns null
        every { mockHomeController.dashboardDefaultToSchedule } returns true
        every { mockHomeController.defaultSeason } returns Year.now().value
        every { mockHomeController.serverDefaultSeason } returns Year.now().value
        every { mockThemeRepository.animationSpeed } returns AnimationSpeed.QUICK
        coEvery { mockEventsRepository.getEvents(any()) } returns flow { emit(listOf(Event.model())) }
        coEvery { mockRaceRepository.shouldSyncRace(any()) } returns false
        coEvery { mockOverviewRepository.fetchOverview(any()) } returns true
        coEvery { mockSeasonRepository.fetchRaces(any()) } returns true
        coEvery { mockSeasonRepository.getDriverStandings(any()) } returns flow { emit(
            SeasonDriverStandings.model()
        ) }
        coEvery { mockSeasonRepository.getConstructorStandings(any()) } returns flow { emit(
            SeasonConstructorStandings.model()
        ) }
        coEvery { mockOverviewRepository.getOverview(any()) } returns flow { emit(
            Overview.model()
        ) }
    }

    @Test
    fun `banner is displayed at top of list`() = coroutineTest {
        every { mockHomeController.banner } returns Banner("msg", "url")

        initSUT()
        sut.inputs.selectSeason(2020)

        sut.outputs.list.test {
            assertListMatchesItem(atIndex = 0) { it is SeasonItem.ErrorItem && it.item is SyncDataItem.Message }
        }
    }

    @Test
    fun `refresh is called immediately if cache is marked out of date`() = coroutineTest {
        every { mockCacheRepository.shouldSyncCurrentSeason() } returns true
        every { mockHomeController.serverDefaultSeason } returns 2020
        runBlockingTest {
            initSUT()
        }

        val observe = sut.outputs.showLoading.testObserve()
        coVerify { mockOverviewRepository.fetchOverview(2020) }
        coVerify { mockSeasonRepository.fetchRaces(2020) }
        coVerify { mockEventsRepository.fetchEvents(2020) }
        verify {
            mockCacheRepository.markedCurrentSeasonSynchronised()
        }
    }

    @Test
    fun `refresh is not called immediately if cache is valid`() = coroutineTest {
        every { mockCacheRepository.shouldSyncCurrentSeason() } returns false
        every { mockHomeController.serverDefaultSeason } returns 2020
        initSUT()
        coVerify(exactly = 0) {
            mockOverviewRepository.fetchOverview(2020)
            mockRaceRepository.fetchRaces(2020)
            mockEventsRepository.fetchEvents(2020)
        }
        verify(exactly = 0) {
            mockCacheRepository.markedCurrentSeasonSynchronised()
        }
    }

    //region List - Schedule / Calendar

    @ParameterizedTest(name = "{0} list empty with network eonnected false shows pull refresh")
    @CsvSource("SCHEDULE","CALENDAR")
    fun `schedule list empty with network connected false shows pull refresh`(navItem: SeasonNavItem) = coroutineTest {
        every { mockOverviewRepository.getOverview(any()) } returns flow {
            emit(Overview.model(season = Year.now().value, overviewRaces = emptyList()))
        }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.selectSeason(Year.now().value)
        sut.inputs.clickItem(navItem)

        sut.outputs.list.test {
            assertValue(listOf(
                SeasonItem.errorModel(SyncDataItem.PullRefresh)
            ))
        }
    }

    @ParameterizedTest(name = "{0} list empty with season being current year shows early in season")
    @CsvSource("SCHEDULE","CALENDAR")
    fun `schedule list empty with season being current year shows early in season`(navItem: SeasonNavItem) = coroutineTest {
        every { mockOverviewRepository.getOverview(any()) } returns flow {
            emit(Overview.model(season = Year.now().value, overviewRaces = emptyList()))
        }

        initSUT()
        sut.inputs.selectSeason(Year.now().value)
        sut.inputs.clickItem(navItem)

        sut.outputs.list.test {
            assertValue(listOf(
                SeasonItem.errorModel(SyncDataItem.Unavailable(DataUnavailable.SEASON_EARLY))
            ))
        }
    }

    @ParameterizedTest(name = "{0} list empty with season being next year shows in future season")
    @CsvSource("SCHEDULE","CALENDAR")
    fun `schedule list empty with season being next year shows in future season`(navItem: SeasonNavItem) = coroutineTest {
        every { mockOverviewRepository.getOverview(any()) } returns flow {
            emit(Overview.model(season = Year.now().value + 1, overviewRaces = emptyList()))
        }

        initSUT()
        sut.inputs.selectSeason(Year.now().value + 1)
        sut.inputs.clickItem(navItem)

        sut.outputs.list.test {
            assertValue(listOf(
                SeasonItem.errorModel(SyncDataItem.Unavailable(DataUnavailable.SEASON_IN_FUTURE))
            ))
        }
    }

    @ParameterizedTest(name = "{0} list empty with season in past shows internal error")
    @CsvSource("SCHEDULE","CALENDAR")
    fun `schedule list empty with season in past shows internal error`(navItem: SeasonNavItem) = coroutineTest {
        every { mockOverviewRepository.getOverview(any()) } returns flow {
            emit(Overview.model(season = Year.now().value - 1, overviewRaces = emptyList()))
        }

        initSUT()
        sut.inputs.selectSeason(Year.now().value - 1)
        sut.inputs.clickItem(navItem)

        sut.outputs.list.test {
            assertValue(listOf(
                SeasonItem.errorModel(SyncDataItem.Unavailable(DataUnavailable.SEASON_INTERNAL_ERROR))
            ))
        }
    }

    //endregion

    //region List - Schedule

    @Test
    fun `schedule list displayed properly`() = coroutineTest {
        every { mockOverviewRepository.getOverview(any()) } returns flow { emit(Overview.model()) }

        initSUT()
        sut.inputs.selectSeason(2020)
        sut.inputs.clickItem(SeasonNavItem.SCHEDULE)

        sut.outputs.list.test {
            assertValue(listOf(
                SeasonItem.eventsModel(),
                SeasonItem.trackModel()
            ))
        }
        verify {
            mockAnalyticsManager.logEvent("select_dashboard_schedule", any())
        }
    }

    //endregion

    //region List - Calendar

    @Test
    fun `calendar list displayed properly`() = coroutineTest {
        every { mockOverviewRepository.getOverview(any()) } returns flow { emit(Overview.model()) }

        initSUT()
        sut.inputs.selectSeason(2020)
        sut.inputs.clickItem(SeasonNavItem.CALENDAR)

        sut.outputs.list.test {
            for (x in 1..12)
            assertListMatchesItem { it is SeasonItem.CalendarMonth && it.month.value == x }
            assertListMatchesItem { it is SeasonItem.CalendarWeek && it.race == OverviewRace.model() }
        }
        verify {
            mockAnalyticsManager.logEvent("select_dashboard_calendar", any())
        }
    }

    //endregion

    //region List - Driver

    @Test
    fun `driver list displayed properly`() = coroutineTest {
        initSUT()
        sut.inputs.selectSeason(Year.now().value)
        sut.inputs.clickItem(SeasonNavItem.DRIVERS)

        sut.outputs.list.test {
            assertValue(listOf(
                SeasonItem.errorModel(SyncDataItem.MessageRes(R.string.results_accurate_for, listOf("name", 1))),
                SeasonItem.driverModel()
            ))
        }
        verify {
            mockAnalyticsManager.logEvent("select_dashboard_driver", any())
        }
    }

    @Test
    fun `driver list empty with network connected false shows pull refresh`() = coroutineTest {
        every { mockSeasonRepository.getDriverStandings(any()) } returns flow {
            emit(SeasonDriverStandings.model(standings = emptyList()))
        }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.selectSeason(Year.now().value)
        sut.inputs.clickItem(SeasonNavItem.DRIVERS)

        sut.outputs.list.test {
            assertValue(listOf(
                SeasonItem.errorModel(SyncDataItem.PullRefresh)
            ))
        }
    }

    @Test
    fun `driver list empty with season being current or next year shows in future season`() = coroutineTest {
        every { mockSeasonRepository.getDriverStandings(any()) } returns flow {
            emit(SeasonDriverStandings.model(standings = emptyList()))
        }

        initSUT()
        sut.inputs.selectSeason(Year.now().value)
        sut.inputs.clickItem(SeasonNavItem.DRIVERS)

        sut.outputs.list.test {
            assertValue(listOf(
                SeasonItem.errorModel(SyncDataItem.Unavailable(DataUnavailable.STANDINGS_EARLY))
            ))
        }
    }

    @Test
    fun `driver list empty with season in past shows internal error`() = coroutineTest {
        every { mockSeasonRepository.getDriverStandings(any()) } returns flow {
            emit(SeasonDriverStandings.model(standings = emptyList()))
        }

        initSUT()
        sut.inputs.selectSeason(Year.now().value - 1)
        sut.inputs.clickItem(SeasonNavItem.DRIVERS)

        sut.outputs.list.test {
            assertValue(listOf(
                SeasonItem.errorModel(SyncDataItem.Unavailable(DataUnavailable.STANDINGS_INTERNAL_ERROR))
            ))
        }
    }

    //endregion

    //region List - Constructor

    @Test
    fun `constructor list displayed properly`() = coroutineTest {
        initSUT()

        runBlockingTest {
            sut.inputs.selectSeason(Year.now().value)
            sut.inputs.clickItem(SeasonNavItem.CONSTRUCTORS)
        }

        sut.outputs.list.test {
            assertValue(listOf(
                SeasonItem.errorModel(SyncDataItem.MessageRes(R.string.results_accurate_for, listOf("name", 1))),
                SeasonItem.constructorModel()
            ))
        }
        verify {
            mockAnalyticsManager.logEvent("select_dashboard_constructor", any())
        }
    }

    @Test
    fun `constructor list empty with network connected false shows pull refresh`() = coroutineTest {
        every { mockSeasonRepository.getConstructorStandings(any()) } returns flow {
            emit(SeasonConstructorStandings.model(standings = emptyList()))
        }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.selectSeason(Year.now().value)
        sut.inputs.clickItem(SeasonNavItem.CONSTRUCTORS)

        sut.outputs.list.test {
            assertValue(listOf(
                SeasonItem.errorModel(SyncDataItem.PullRefresh)
            ))
        }
    }

    @Test
    fun `constructor list empty with season being current or next year shows in future season`() = coroutineTest {
        every { mockSeasonRepository.getConstructorStandings(any()) } returns flow {
            emit(SeasonConstructorStandings.model(standings = emptyList()))
        }

        initSUT()
        sut.inputs.selectSeason(Year.now().value)
        sut.inputs.clickItem(SeasonNavItem.CONSTRUCTORS)

        sut.outputs.list.test {
            assertValue(listOf(
                SeasonItem.errorModel(SyncDataItem.Unavailable(DataUnavailable.STANDINGS_EARLY))
            ))
        }
    }

    @Test
    fun `constructor list empty with season in past shows internal error`() = coroutineTest {
        every { mockSeasonRepository.getConstructorStandings(any()) } returns flow {
            emit(SeasonConstructorStandings.model(standings = emptyList()))
        }

        initSUT()
        sut.inputs.selectSeason(Year.now().value - 1)
        sut.inputs.clickItem(SeasonNavItem.CONSTRUCTORS)

        sut.outputs.list.test {
            assertValue(listOf(
                SeasonItem.errorModel(SyncDataItem.Unavailable(DataUnavailable.STANDINGS_INTERNAL_ERROR))
            ))
        }
    }

    //endregion

    //region Request

    @Test
    fun `season and overview request is not made when should refresh is false`() = coroutineTest {
        coEvery { mockRaceRepository.shouldSyncRace(any()) } returns false

        initSUT()
        runBlockingTest {
            sut.inputs.selectSeason(2020)
        }

        val observe = sut.outputs.list.testObserve()
        coVerify(exactly = 0) {
            mockOverviewRepository.fetchOverview(any())
            mockSeasonRepository.fetchRaces(any())
        }
    }

    @Test
    fun `season and overview request is made when should refresh is true`() = coroutineTest {
        coEvery { mockRaceRepository.shouldSyncRace(any()) } returns true

        initSUT()
        runBlockingTest {
            sut.inputs.selectSeason(2020)
        }

        val observe = sut.outputs.list.testObserve()
        coVerify {
            mockOverviewRepository.fetchOverview(any())
            mockSeasonRepository.fetchRaces(any())
        }
    }

    //endregion

    //region Track select

    @Test
    fun `click track fires open race event`() {
        val input = SeasonItem.trackModel()
        initSUT()
        sut.inputs.clickTrack(input)

        sut.outputs.openRace.test {
            assertDataEventValue(input)
        }
    }

    //endregion

    //region Driver select

    @Test
    fun `click driver fires open driver event`() {
        val input = SeasonItem.driverModel()
        initSUT()
        sut.inputs.clickDriver(input)

        sut.outputs.openDriver.test {
            assertDataEventValue(input)
        }
    }

    //endregion

    //region Constructor select

    @Test
    fun `click constructor fires open constructor event`() {
        val input = SeasonItem.constructorModel()
        initSUT()
        sut.inputs.clickConstructor(input)

        sut.outputs.openConstructor.test {
            assertDataEventValue(input)
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh alls overview and season repository and shows loading false when done`() = coroutineTest {
        initSUT()
        sut.inputs.selectSeason(2020)

        runBlockingTest {
            sut.inputs.refresh()
        }

        coVerify {
            mockOverviewRepository.fetchOverview(any())
            mockSeasonRepository.fetchRaces(any())
        }
        sut.outputs.showLoading.test {
            assertValue(false)
        }
    }

    //endregion

}