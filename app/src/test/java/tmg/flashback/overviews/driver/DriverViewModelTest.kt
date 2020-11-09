package tmg.flashback.overviews.driver

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.R
import tmg.flashback.currentYear
import tmg.flashback.overviews.*
import tmg.flashback.overviews.driver.summary.DriverSummaryItem
import tmg.flashback.overviews.driver.summary.PipeType.*
import tmg.flashback.repo.db.stats.DriverDB
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.sync.SyncDataItem
import tmg.flashback.shared.viewholders.DataUnavailable
import tmg.flashback.testutils.*
import tmg.flashback.utils.position

@FlowPreview
@ExperimentalCoroutinesApi
class DriverViewModelTest: BaseTest() {

    lateinit var sut: DriverViewModel

    private var mockDriverDB: DriverDB = mock()
    private var mockConnectivityManager: ConnectivityManager = mock()

    @BeforeEach
    internal fun setUp() {

        whenever(mockConnectivityManager.isConnected).thenReturn(true)
    }

    private fun initSUT() {
        sut = DriverViewModel(mockDriverDB, mockConnectivityManager, testScopeProvider)
        sut.inputs.setup(mockDriverId)
    }

    @Test
    fun `DriverViewModel setup loads error state when network connectivity is down`() = coroutineTest {

        whenever(mockConnectivityManager.isConnected).thenReturn(false)
        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(null) })
        val expected = listOf(
            DriverSummaryItem.ErrorItem(
                SyncDataItem.NoNetwork
            )
        )

        initSUT()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `DriverViewModel setup loads an error state when driver overview is returned`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(null) })
        val expected = listOf(
            DriverSummaryItem.ErrorItem(
                SyncDataItem.Unavailable(DataUnavailable.DRIVER_NOT_EXIST)
            )
        )

        initSUT()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `DriverViewModel setup which contains championship item in progress`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverviewChampionshipInProgress) })

        initSUT()

        assertListContains(sut.outputs.list) {
            it is DriverSummaryItem.ErrorItem &&
                    it.item is SyncDataItem.MessageRes &&
                    (it.item as SyncDataItem.MessageRes).msg == R.string.results_accurate_for_year
        }
    }

    @Test
    fun `DriverViewModel contains header item with appropiate mock data`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverview) })

        initSUT()

        assertListContains(sut.outputs.list) {
            if (it is DriverSummaryItem.Header) {
                assertEquals(mockDriverFirstName, it.driverFirstname)
                assertEquals(mockDriverLastName, it.driverSurname)
                assertEquals(mockDriverNumber, it.driverNumber)
                assertEquals(mockDriverPhotoUrl, it.driverImg)
                assertEquals(mockDriverDateOfBirth, it.driverBirthday)
                assertEquals(mockDriverWikiUrl, it.driverWikiUrl)
                assertEquals(mockDriverNationalityISO, it.driverNationalityISO)
                return@assertListContains true
            }
            return@assertListContains false
        }
    }

    @Test
    fun `DriverViewModel list contains highlighted championship quantity result when driver has championships`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverviewWonChampionship) })

        initSUT()

        assertListContains(sut.outputs.list) {
            it == DriverSummaryItem.Stat(
                tint = R.attr.f1Favourite,
                icon = R.drawable.ic_menu_drivers,
                label = R.string.driver_overview_stat_career_drivers_title,
                value = "1"
            )
        }
    }

    @Test
    fun `DriverViewModel list contains non highlighted championship quantity result when driver has not won championships`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverviewNotWonChampionship) })

        initSUT()

        assertListContains(sut.outputs.list) {
            it == DriverSummaryItem.Stat(
                icon = R.drawable.ic_menu_drivers,
                label = R.string.driver_overview_stat_career_drivers_title,
                value = "0"
            )
        }
    }

    @Test
    fun `DriverViewModel list doesn't contain career best championship if driver is in rookie season`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverviewRookieSeason) })

        initSUT()

        assertListDoesntContains(sut.outputs.list) {
            it == DriverSummaryItem.Stat(
                icon = R.drawable.ic_championship_order,
                label = R.string.driver_overview_stat_career_best_championship_position,
                value = "1st"
            )
        }
    }


    @Test
    fun `DriverViewModel list contains career best championship if driver has completed a season`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverview) })

        initSUT()

        assertListContains(sut.outputs.list) {
            it == DriverSummaryItem.Stat(
                icon = R.drawable.ic_championship_order,
                label = R.string.driver_overview_stat_career_best_championship_position,
                value = "1st"
            )
        }
    }

    @Test
    fun `DriverViewModel setup loads standard list of statistics items`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverview) })
        val expected = listOf(
                expectedDriverHeader,
            DriverSummaryItem.Stat(
                icon = R.drawable.ic_standings,
                label = R.string.driver_overview_stat_career_wins,
                value = mockDriverOverview.careerWins.toString()
            ),
            DriverSummaryItem.Stat(
                icon = R.drawable.ic_podium,
                label = R.string.driver_overview_stat_career_podiums,
                value = mockDriverOverview.careerPodiums.toString()
            ),
            DriverSummaryItem.Stat(
                icon = R.drawable.ic_status_finished,
                label = R.string.driver_overview_stat_career_best_finish,
                value = mockDriverOverview.careerBestFinish.position()
            ),
            DriverSummaryItem.Stat(
                icon = R.drawable.ic_finishes_in_points,
                label = R.string.driver_overview_stat_career_points_finishes,
                value = mockDriverOverview.careerFinishesInPoints.toString()
            ),
            DriverSummaryItem.Stat(
                icon = R.drawable.ic_race_points,
                label = R.string.driver_overview_stat_career_points,
                value = mockDriverOverview.careerPoints.toString()
            ),
            DriverSummaryItem.Stat(
                icon = R.drawable.ic_qualifying_pole,
                label = R.string.driver_overview_stat_career_qualifying_pole,
                value = mockDriverOverview.careerQualifyingPoles.toString()
            ),
            DriverSummaryItem.Stat(
                icon = R.drawable.ic_qualifying_front_row,
                label = R.string.driver_overview_stat_career_qualifying_top_3,
                value = mockDriverOverview.careerQualifyingTop3.toString()
            ),
            DriverSummaryItem.Stat(
                icon = R.drawable.ic_qualifying_top_ten,
                label = R.string.driver_overview_stat_career_qualifying_top_10,
                value = mockDriverOverview.totalQualifyingAbove(10).toString()
            ),
            DriverSummaryItem.Stat(
                icon = R.drawable.ic_team,
                label = R.string.driver_overview_stat_career_team_history,
                value = ""
            )
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)
    }

    @Test
    fun `DriverViewModel team ordering and highlighting default setup`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverviewConstructorChangeThenYearOffEndingInCurrentSeason) })
        val expected = listOf<DriverSummaryItem>(
            DriverSummaryItem.RacedFor(
                currentYear,
                    mockDriverOverviewConstructor2,
                START_END,
                false
            ),
                // TODO: Add this when it's 2022 - DriverOverviewItem.RacedFor(2021, mockDriverOverviewConstructor, SEASON, false),
                // TODO: Add this when it's 2021 - DriverOverviewItem.RacedFor(2020, mockDriverOverviewConstructor, SEASON, false),
            DriverSummaryItem.RacedFor(
                2019,
                    mockDriverOverviewConstructor,
                START_END,
                false
            ),
            DriverSummaryItem.RacedFor(
                2018,
                    mockDriverOverviewConstructor2,
                SINGLE_PIPE,
                false
            ),
            DriverSummaryItem.RacedFor(
                2018,
                    mockDriverOverviewConstructor,
                START_END,
                false
            ),
            DriverSummaryItem.RacedFor(
                2017,
                    mockDriverOverviewConstructor,
                START_END,
                false
            ),
            DriverSummaryItem.RacedFor(
                2016,
                    mockDriverOverviewConstructor,
                SINGLE_PIPE,
                false
            ),
            DriverSummaryItem.RacedFor(
                2016,
                    mockDriverOverviewConstructor2,
                SINGLE_PIPE,
                false
            ),
            DriverSummaryItem.RacedFor(
                2016,
                    mockDriverOverviewConstructor,
                END,
                false
            ),
            DriverSummaryItem.RacedFor(
                2014,
                    mockDriverOverviewConstructor,
                START,
                false
            ),
            DriverSummaryItem.RacedFor(
                2013,
                    mockDriverOverviewConstructor2,
                SINGLE_PIPE,
                false
            ),
            DriverSummaryItem.RacedFor(
                2013,
                    mockDriverOverviewConstructor,
                END,
                false
            ),
            DriverSummaryItem.RacedFor(
                2011,
                    mockDriverOverviewConstructor,
                SINGLE,
                true
            )
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)
    }

    @Test
    fun `DriverViewModel single team shown when 1 season for 1 year then year off`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverviewTwoSeasonWithYearBetweenThem) })
        val expected = listOf(
            DriverSummaryItem.RacedFor(
                2019,
                    mockDriverOverviewConstructor,
                SINGLE,
                true
            ),
            DriverSummaryItem.RacedFor(
                2017,
                    mockDriverOverviewConstructor,
                SINGLE,
                false
            )
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)
    }

    @Test
    fun `DriverViewModel single team shown when 1 season for 1 year whilst current year in progress`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverviewTwoSeasonWithYearBetweenThemEndingInCurrentYear) })
        val expected = listOf(
            DriverSummaryItem.RacedFor(
                currentYear,
                    mockDriverOverviewConstructor,
                END,
                false
            ),
            DriverSummaryItem.RacedFor(
                2017,
                    mockDriverOverviewConstructor,
                SINGLE,
                true
            )
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)
    }

    @Test
    fun `DriverViewModel clicking open season opens season for driver`() = coroutineTest {

        val expected = 2020

        initSUT()

        sut.inputs.openSeason(expected)

        assertDataEventValue(Pair(mockDriverId, expected), sut.outputs.openSeason)
    }

    @Test
    fun `DriverViewModel clicking open url forwards open url event`() = coroutineTest {

        val expectUrl = "http://www.google.com"

        initSUT()

        sut.inputs.openUrl(expectUrl)

        assertDataEventValue(expectUrl, sut.outputs.openUrl)
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockDriverDB, mockConnectivityManager)
    }

    // Expected list for mockDriverOverview

    private val expectedDriverHeader: DriverSummaryItem.Header =
        DriverSummaryItem.Header(
            driverFirstname = mockDriverFirstName,
            driverSurname = mockDriverLastName,
            driverNumber = mockDriverNumber,
            driverImg = mockDriverPhotoUrl ?: "",
            driverBirthday = mockDriverDateOfBirth,
            driverWikiUrl = mockDriverWikiUrl,
            driverNationalityISO = mockDriverNationalityISO
        )
}