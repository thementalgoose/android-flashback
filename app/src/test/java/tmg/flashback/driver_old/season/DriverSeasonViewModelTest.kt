package tmg.flashback.driver_old.season

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.R
import tmg.flashback.driver_old.*
import tmg.flashback.driver.overview.RaceForPositionType.*
import tmg.flashback.driver.season.DriverSeasonItem
import tmg.flashback.driver.season.DriverSeasonViewModel
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.stats.DriverDB
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.models.stats.DriverOverviewRace
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.SyncDataItem
import tmg.flashback.shared.viewholders.DataUnavailable.DRIVER_NOT_EXIST
import tmg.flashback.testutils.*
import tmg.flashback.utils.position

@FlowPreview
@ExperimentalCoroutinesApi
class DriverSeasonViewModelTest: BaseTest() {

    lateinit var sut: DriverSeasonViewModel

    private var mockDriverDB: DriverDB = mock()
    private var mockConnectivityManager: ConnectivityManager = mock()
    private var mockPrefsDB: PrefsDB = mock()

    @BeforeEach
    internal fun setUp() {

        whenever(mockConnectivityManager.isConnected).thenReturn(true)
        whenever(mockPrefsDB.barAnimation).thenReturn(BarAnimation.NONE)
    }

    private fun initSUT() {
        sut = DriverSeasonViewModel(
            mockDriverDB,
            mockConnectivityManager,
            mockPrefsDB,
            testScopeProvider
        )
        sut.inputs.setup(mockDriverId, 2019)
    }

    @Test
    fun `DriverSeasonViewModel no network connection shows no network error`() = coroutineTest {

        whenever(mockConnectivityManager.isConnected).thenReturn(false)
        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(null) })
        val expected = listOf<DriverSeasonItem>(
                DriverSeasonItem.ErrorItem(SyncDataItem.NoNetwork)
        )

        initSUT()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `DriverSeasonViewModel no driver overview found but valid network connections shows driver not exist error`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(null) })
        val expected = listOf<DriverSeasonItem>(
                DriverSeasonItem.ErrorItem(SyncDataItem.Unavailable(DRIVER_NOT_EXIST))
        )

        initSUT()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `DriverSeasonViewModel init shows driver split when theres one constructor changes`() = coroutineTest {

        val conditionedDriverOverview = mockDriverOverview.copy(
                standings = listOf(mockDriverOverview2019Standing.copy(
                        constructors = listOf(
                                mockDriverOverviewConstructor
                        )
                ))
        )
        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(conditionedDriverOverview) })
        val expected = listOf(
                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor, SINGLE, false)
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)

    }

    @Test
    fun `DriverSeasonViewModel init shows driver split when theres two constructor changes`() = coroutineTest {

        val conditionedDriverOverview = mockDriverOverview.copy(
                standings = listOf(mockDriverOverview2019Standing.copy(
                        constructors = listOf(
                                mockDriverOverviewConstructor,
                                mockDriverOverviewConstructor2
                        )
                ))
        )
        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(conditionedDriverOverview) })
        val expected = listOf(
                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor2, START, false),
                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor, END, false)
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)
    }

    @Test
    fun `DriverSeasonViewModel init shows driver split when theres three constructor changes`() = coroutineTest {

        val conditionedDriverOverview = mockDriverOverview.copy(
                standings = listOf(mockDriverOverview2019Standing.copy(
                        constructors = listOf(
                                mockDriverOverviewConstructor,
                                mockDriverOverviewConstructor3,
                                mockDriverOverviewConstructor2
                        )
                ))
        )
        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(conditionedDriverOverview) })
        val expected = listOf(
                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor2, START, false),
                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor3, MID_SEASON_CHANGE, false),
                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor, END, false)
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)
    }

    @Test
    fun `DriverSeasonViewModel init for season contains season summary`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverview) })
        val expected = listOf(
                DriverSeasonItem.Stat(icon = R.drawable.ic_team, label = R.string.driver_overview_stat_career_team, value = ""),
                DriverSeasonItem.Stat(icon = R.drawable.ic_standings, label = R.string.driver_overview_stat_career_wins, value = mockDriverOverview2019Standing.wins.toString()),
                DriverSeasonItem.Stat(icon = R.drawable.ic_podium, label = R.string.driver_overview_stat_career_podiums, value = mockDriverOverview2019Standing.podiums.toString()),
                DriverSeasonItem.Stat(icon = R.drawable.ic_status_finished, label = R.string.driver_overview_stat_career_best_finish, value = mockDriverOverview2019Standing.bestFinish.position()),
                DriverSeasonItem.Stat(icon = R.drawable.ic_finishes_in_points, label = R.string.driver_overview_stat_career_points_finishes, value = mockDriverOverview2019Standing.finishesInPoints.toString()),
                DriverSeasonItem.Stat(icon = R.drawable.ic_race_points, label = R.string.driver_overview_stat_career_points, value = mockDriverOverview2019Standing.points.toString()),
                DriverSeasonItem.Stat(icon = R.drawable.ic_qualifying_pole, label = R.string.driver_overview_stat_career_qualifying_pole, value = mockDriverOverview2019Standing.qualifyingPoles.toString()),
                DriverSeasonItem.Stat(icon = R.drawable.ic_qualifying_front_row, label = R.string.driver_overview_stat_career_qualifying_top_3, value = mockDriverOverview2019Standing.qualifyingTop3.toString()),
                DriverSeasonItem.Stat(icon = R.drawable.ic_qualifying_top_ten, label = R.string.driver_overview_stat_career_qualifying_top_10, value = mockDriverOverview2019Standing.totalQualifyingAbove(10).toString())
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)
    }


    @Test
    fun `DriverSeasonViewModel init contains result header `() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverview) })
        val expected = listOf(
                DriverSeasonItem.ResultHeader
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)
    }


    @Test
    fun `DriverSeasonViewModel init contains race overview summary cards`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverview) })
        val expected = listOf(
                DriverSeasonItem.ResultHeader
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)
    }



    @Test
    fun `DriverSeasonViewModel init contains rounds that summarise driver overview`() = coroutineTest {
        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverview) })
        val expected = listOf<DriverSeasonItem>(
                expectedFirstRound,
                expectedSecondRound
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)
    }


    @Test
    fun `DriverSeasonViewModel clicking season round fires go to season round event`() = coroutineTest {

        initSUT()

        sut.inputs.clickSeasonRound(expectedFirstRound)

        assertDataEventValue(expectedFirstRound, sut.outputs.openSeasonRound)
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockDriverDB, mockConnectivityManager)
    }

    val expectedFirstRound: DriverSeasonItem.Result
        get() = convertDriverOverviewRace(mockDriverOverviewRaceFirst)

    val expectedSecondRound: DriverSeasonItem.Result
        get() = convertDriverOverviewRace(mockDriverOverviewRaceSecond)

    private fun convertDriverOverviewRace(race: DriverOverviewRace): DriverSeasonItem.Result {
        return DriverSeasonItem.Result(
                season = race.season,
                round = race.round,
                raceName = race.raceName,
                circuitName = race.circuitName,
                circuitId = race.circuitId,
                raceCountry = race.circuitNationality,
                raceCountryISO = race.circuitNationalityISO,
                constructor = mockDriverOverviewConstructor,
                date = race.date,
                qualified = race.qualified,
                finished = race.finished,
                raceStatus = race.status,
                points = race.points,
                maxPoints = 25,
                barAnimation = BarAnimation.NONE
        )
    }
}
