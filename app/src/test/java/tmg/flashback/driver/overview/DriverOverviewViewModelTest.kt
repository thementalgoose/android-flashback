package tmg.flashback.driver.overview

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
import tmg.flashback.driver.*
import tmg.flashback.driver.overview.RaceForPositionType.*
import tmg.flashback.repo.db.stats.DriverDB
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.SyncDataItem
import tmg.flashback.shared.viewholders.DataUnavailable
import tmg.flashback.testutils.*
import tmg.flashback.utils.position

@FlowPreview
@ExperimentalCoroutinesApi
class DriverOverviewViewModelTest: BaseTest() {

    lateinit var sut: DriverOverviewViewModel

    private var mockDriverDB: DriverDB = mock()
    private var mockConnectivityManager: ConnectivityManager = mock()

    @BeforeEach
    internal fun setUp() {

        whenever(mockConnectivityManager.isConnected).thenReturn(true)
    }

    private fun initSUT() {
        sut = DriverOverviewViewModel(mockDriverDB, mockConnectivityManager, testScopeProvider)
        sut.inputs.setup(mockDriverId)
    }

    @Test
    fun `DriverOverviewViewModel setup loads error state when network connectivity is down`() = coroutineTest {

        whenever(mockConnectivityManager.isConnected).thenReturn(false)
        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(null) })
        val expected = listOf(
                DriverOverviewItem.ErrorItem(SyncDataItem.NoNetwork)
        )

        initSUT()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `DriverOverviewViewModel setup loads an error state when driver overview is returned`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(null) })
        val expected = listOf(
                DriverOverviewItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.DRIVER_NOT_EXIST))
        )

        initSUT()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `DriverOverviewViewModel setup which contains championship item in progress`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverviewChampionshipInProgress) })

        initSUT()

        assertListContains(sut.outputs.list) {
            it is DriverOverviewItem.ErrorItem &&
                    it.item is SyncDataItem.MessageRes &&
                    (it.item as SyncDataItem.MessageRes).msg == R.string.results_accurate_for_year
        }
    }

    @Test
    fun `DriverOverviewViewModel contains header item with appropiate mock data`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverview) })

        initSUT()

        assertListContains(sut.outputs.list) {
            if (it is DriverOverviewItem.Header) {
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
    fun `DriverOverviewViewModel list contains highlighted championship quantity result when driver has championships`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverviewWonChampionship) })

        initSUT()

        assertListContains(sut.outputs.list) {
            it == DriverOverviewItem.Stat(
                    tint = R.attr.f1Favourite,
                    icon = R.drawable.ic_menu_drivers,
                    label = R.string.driver_overview_stat_career_drivers_title,
                    value = "1"
            )
        }
    }

    @Test
    fun `DriverOverviewViewModel list contains non highlighted championship quantity result when driver has not won championships`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverviewNotWonChampionship) })

        initSUT()

        assertListContains(sut.outputs.list) {
            it == DriverOverviewItem.Stat(
                    icon = R.drawable.ic_menu_drivers,
                    label = R.string.driver_overview_stat_career_drivers_title,
                    value = "0"
            )
        }
    }

    @Test
    fun `DriverOverviewViewModel list doesn't contain career best championship if driver is in rookie season`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverviewRookieSeason) })

        initSUT()

        assertListNotContains(sut.outputs.list) {
            it == DriverOverviewItem.Stat(
                    icon = R.drawable.ic_championship_order,
                    label = R.string.driver_overview_stat_career_best_championship_position,
                    value = "1st"
            )
        }
    }


    @Test
    fun `DriverOverviewViewModel list contains career best championship if driver has completed a season`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverview) })

        initSUT()

        assertListContains(sut.outputs.list) {
            it == DriverOverviewItem.Stat(
                    icon = R.drawable.ic_championship_order,
                    label = R.string.driver_overview_stat_career_best_championship_position,
                    value = "1st"
            )
        }
    }

    @Test
    fun `DriverOverviewViewModel setup loads standard list of statistics items`() = coroutineTest {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverview) })
        val expected = listOf(
                expectedDriverHeader,
                DriverOverviewItem.Stat(icon = R.drawable.ic_standings, label = R.string.driver_overview_stat_career_wins, value = mockDriverOverview.careerWins.toString()),
                DriverOverviewItem.Stat(icon = R.drawable.ic_podium, label = R.string.driver_overview_stat_career_podiums, value = mockDriverOverview.careerPodiums.toString()),
                DriverOverviewItem.Stat(icon = R.drawable.ic_status_finished, label = R.string.driver_overview_stat_career_best_finish, value = mockDriverOverview.careerBestFinish.position()),
                DriverOverviewItem.Stat(icon = R.drawable.ic_finishes_in_points, label = R.string.driver_overview_stat_career_points_finishes, value = mockDriverOverview.careerFinishesInPoints.toString()),
                DriverOverviewItem.Stat(icon = R.drawable.ic_race_points, label = R.string.driver_overview_stat_career_points, value = mockDriverOverview.careerPoints.toString()),
                DriverOverviewItem.Stat(icon = R.drawable.ic_qualifying_pole, label = R.string.driver_overview_stat_career_qualifying_pole, value = mockDriverOverview.careerQualifyingPoles.toString()),
                DriverOverviewItem.Stat(icon = R.drawable.ic_qualifying_front_row, label = R.string.driver_overview_stat_career_qualifying_top_3, value = mockDriverOverview.careerQualifyingTop3.toString()),
                DriverOverviewItem.Stat(icon = R.drawable.ic_qualifying_top_ten, label = R.string.driver_overview_stat_career_qualifying_top_10, value = mockDriverOverview.totalQualifyingAbove(10).toString()),
                DriverOverviewItem.Stat(icon = R.drawable.ic_team, label = R.string.driver_overview_stat_career_team_history, value = "")
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)
    }

    @Test
    fun `DriverOverviewViewModel team ordering and highlighting default setup`() {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverviewConstructorChangeThenYearOffEndingInCurrentSeason) })
        val expected = listOf<DriverOverviewItem>(
                DriverOverviewItem.RacedFor(currentYear, mockDriverOverviewConstructor2, SEASON, false),
                // TODO: Add this when it's 2022 - DriverOverviewItem.RacedFor(2021, mockDriverOverviewConstructor, SEASON, false),
                // TODO: Add this when it's 2021 - DriverOverviewItem.RacedFor(2020, mockDriverOverviewConstructor, SEASON, false),
                DriverOverviewItem.RacedFor(2019, mockDriverOverviewConstructor, SEASON, false),
                DriverOverviewItem.RacedFor(2018, mockDriverOverviewConstructor2, MID_SEASON_CHANGE, false),
                DriverOverviewItem.RacedFor(2018, mockDriverOverviewConstructor, SEASON, false),
                DriverOverviewItem.RacedFor(2017, mockDriverOverviewConstructor, SEASON, false),
                DriverOverviewItem.RacedFor(2016, mockDriverOverviewConstructor, MID_SEASON_CHANGE, false),
                DriverOverviewItem.RacedFor(2016, mockDriverOverviewConstructor2, MID_SEASON_CHANGE, false),
                DriverOverviewItem.RacedFor(2016, mockDriverOverviewConstructor, END, false),
                DriverOverviewItem.RacedFor(2014, mockDriverOverviewConstructor, START, false),
                DriverOverviewItem.RacedFor(2013, mockDriverOverviewConstructor2, MID_SEASON_CHANGE, false),
                DriverOverviewItem.RacedFor(2013, mockDriverOverviewConstructor, END, false),
                DriverOverviewItem.RacedFor(2011, mockDriverOverviewConstructor, SINGLE, true)
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)
    }

    @Test
    fun `DriverOverviewViewModel single team shown when 1 season for 1 year then year off`() {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverviewTwoSeasonWithYearBetweenThem) })
        val expected = listOf(
                DriverOverviewItem.RacedFor(2019, mockDriverOverviewConstructor, SINGLE, true),
                DriverOverviewItem.RacedFor(2017, mockDriverOverviewConstructor, SINGLE, false)
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)
    }

    @Test
    fun `DriverOverviewViewModel single team shown when 1 season for 1 year whilst current year in progress`() {

        whenever(mockDriverDB.getDriverOverview(any())).thenReturn(flow { emit(mockDriverOverviewTwoSeasonWithYearBetweenThemEndingInCurrentYear) })
        val expected = listOf(
                DriverOverviewItem.RacedFor(currentYear, mockDriverOverviewConstructor, END, false),
                DriverOverviewItem.RacedFor(2017, mockDriverOverviewConstructor, SINGLE, true)
        )

        initSUT()

        assertListContainsValues(sut.outputs.list, expected)
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockDriverDB, mockConnectivityManager)
    }

    // Expected list for mockDriverOverview

    private val expectedDriverHeader: DriverOverviewItem.Header = DriverOverviewItem.Header(
            driverFirstname = mockDriverFirstName,
            driverSurname = mockDriverLastName,
            driverNumber = mockDriverNumber,
            driverImg = mockDriverPhotoUrl ?: "",
            driverBirthday = mockDriverDateOfBirth,
            driverWikiUrl = mockDriverWikiUrl,
            driverNationalityISO = mockDriverNationalityISO
    )
}