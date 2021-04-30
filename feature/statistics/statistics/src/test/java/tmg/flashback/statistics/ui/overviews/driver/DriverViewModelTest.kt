package tmg.flashback.statistics.ui.overviews.driver

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.Year
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.statistics.ui.overview.driver.summary.DriverSummaryItem
import tmg.flashback.statistics.ui.overview.driver.summary.PipeType
import tmg.flashback.statistics.ui.overview.driver.summary.PipeType.*
import tmg.flashback.data.db.stats.DriverRepository
import tmg.flashback.statistics.R
import tmg.flashback.statistics.constants.Formula1.currentSeasonYear
import tmg.flashback.statistics.testutils.*
import tmg.flashback.statistics.testutils.BaseTest
import tmg.flashback.statistics.testutils.assertListContainsItem
import tmg.flashback.statistics.testutils.assertListMatchesItem
import tmg.flashback.statistics.testutils.test
import tmg.flashback.statistics.ui.overview.driver.DriverViewModel
import tmg.flashback.statistics.ui.overviews.*
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.flashback.statistics.ui.util.position

internal class DriverViewModelTest: BaseTest() {

    lateinit var sut: DriverViewModel

    private var mockDriverRepository: DriverRepository = mockk()
    private var mockConnectivityManager: NetworkConnectivityManager = mockk()

    @BeforeEach
    internal fun setUp() {

        every { mockConnectivityManager.isConnected } returns true
    }

    private fun initSUT() {
        sut = DriverViewModel(mockDriverRepository, mockConnectivityManager)
        sut.inputs.setup(mockDriverId)
    }

    @Test
    fun `setup loads error state when network connectivity is down`() = coroutineTest {

        every { mockConnectivityManager.isConnected } returns false
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(null) }

        val expected = listOf(
            DriverSummaryItem.ErrorItem(SyncDataItem.NoNetwork),
            DriverSummaryItem.ErrorItem(SyncDataItem.ProvidedBy())
        )

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `setup loads an error state when driver overview is returned as null`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(null) }
        val expected = listOf(
            DriverSummaryItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.DRIVER_NOT_EXIST)),
            DriverSummaryItem.ErrorItem(SyncDataItem.ProvidedBy())
        )

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `setup which contains championship item in progress`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
            mockDriverOverviewChampionshipInProgress
        ) }

        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem {
                it is DriverSummaryItem.ErrorItem &&
                        it.item is SyncDataItem.MessageRes &&
                        (it.item as SyncDataItem.MessageRes).msg == R.string.results_accurate_for_year
            }
        }
    }

    @Test
    fun `contains header item with appropriate mock data`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
            mockDriverOverview
        ) }

        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem {
                if (it is DriverSummaryItem.Header) {
                    assertEquals(mockDriverFirstName, it.driverFirstname)
                    assertEquals(mockDriverLastName, it.driverSurname)
                    assertEquals(mockDriverNumber, it.driverNumber)
                    assertEquals(mockDriverPhotoUrl, it.driverImg)
                    assertEquals(mockDriverDateOfBirth, it.driverBirthday)
                    assertEquals(mockDriverWikiUrl, it.driverWikiUrl)
                    assertEquals(mockDriverNationalityISO, it.driverNationalityISO)
                    return@assertListMatchesItem true
                }
                return@assertListMatchesItem false
            }
        }
    }

    @Test
    fun `list contains highlighted championship quantity result when driver has championships`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
            mockDriverOverviewWonChampionship
        ) }

        initSUT()

        sut.outputs.list.test {
            assertListContainsItem(
                DriverSummaryItem.Stat(
                tint = R.attr.f1Favourite,
                icon = R.drawable.ic_menu_drivers,
                label = R.string.driver_overview_stat_career_drivers_title,
                value = "1"
            ))
        }
    }

    @Test
    fun `list contains non highlighted championship quantity result when driver has not won championships`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
            mockDriverOverviewNotWonChampionship
        ) }

        initSUT()

        sut.outputs.list.test {
            assertListContainsItem(
                DriverSummaryItem.Stat(
                icon = R.drawable.ic_menu_drivers,
                label = R.string.driver_overview_stat_career_drivers_title,
                value = "0"
            ))
        }
    }

    @Test
    fun `list doesn't contain career best championship if driver is in rookie season`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
            mockDriverOverviewRookieSeason
        ) }

        initSUT()

        sut.outputs.list.test {
            assertListExcludesItem(
                DriverSummaryItem.Stat(
                icon = R.drawable.ic_championship_order,
                label = R.string.driver_overview_stat_career_best_championship_position,
                value = "1st"
            ))
        }
    }


    @Test
    fun `list contains career best championship if driver has completed a season`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
            mockDriverOverview
        ) }

        initSUT()

        sut.outputs.list.test {
            assertListContainsItem(
                DriverSummaryItem.Stat(
                icon = R.drawable.ic_championship_order,
                label = R.string.driver_overview_stat_career_best_championship_position,
                value = "1st"
            ))
        }
    }

    @Test
    fun `setup loads standard list of statistics items`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
            mockDriverOverview
        ) }
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
                icon = R.drawable.ic_race_starts,
                label = R.string.driver_overview_stat_race_starts,
                value = mockDriverOverview.raceStarts.toString()
            ),
            DriverSummaryItem.Stat(
                icon = R.drawable.ic_race_finishes,
                label = R.string.driver_overview_stat_race_finishes,
                value = mockDriverOverview.raceFinishes.toString()
            ),
            DriverSummaryItem.Stat(
                icon = R.drawable.ic_race_retirements,
                label = R.string.driver_overview_stat_race_retirements,
                value = mockDriverOverview.raceRetirements.toString()
            ),
            DriverSummaryItem.Stat(
                icon = R.drawable.ic_best_finish,
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

        sut.outputs.list.test {
            assertListContainsItems(expected)
        }
    }

    @Test
    fun `team ordering and highlighting default setup`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
            mockDriverOverviewConstructorChangeThenYearOffEndingInCurrentSeason
        ) }

        val expected = listOf<DriverSummaryItem>(
            DriverSummaryItem.RacedFor(
                Year.now().value,
                listOf(mockDriverOverviewConstructor2),
                START,
                false
            ),
            DriverSummaryItem.RacedFor(
                Year.now().value - 1,
                listOf(mockDriverOverviewConstructor),
                START_END,
                false
            ),
            DriverSummaryItem.RacedFor(
                Year.now().value - 2,
                listOf(mockDriverOverviewConstructor2, mockDriverOverviewConstructor),
                START_END,
                false
            ),
            DriverSummaryItem.RacedFor(
                Year.now().value - 3,
                listOf(mockDriverOverviewConstructor),
                START_END,
                false
            ),
            DriverSummaryItem.RacedFor(
                Year.now().value - 4,
                listOf(mockDriverOverviewConstructor, mockDriverOverviewConstructor2, mockDriverOverviewConstructor),
                END,
                false
            ),
            DriverSummaryItem.RacedFor(
                2014,
                listOf(mockDriverOverviewConstructor),
                START,
                false
            ),
            DriverSummaryItem.RacedFor(
                2013,
                listOf(mockDriverOverviewConstructor2, mockDriverOverviewConstructor),
                END,
                false
            ),
            DriverSummaryItem.RacedFor(
                2011,
                listOf(mockDriverOverviewConstructor),
                SINGLE,
                true
            )
        )

        initSUT()

        sut.outputs.list.test {
            assertListContainsItems(expected)
        }
    }

    @Test
    fun `single team shown when 1 season for 1 year then year off`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
            mockDriverOverviewTwoSeasonWithYearBetweenThem
        ) }

        val expected = listOf(
            DriverSummaryItem.RacedFor(
                2019,
                    listOf(mockDriverOverviewConstructor),
                SINGLE,
                true
            ),
            DriverSummaryItem.RacedFor(
                2017,
                    listOf(mockDriverOverviewConstructor),
                SINGLE,
                false
            )
        )

        initSUT()

        sut.outputs.list.test {
            assertListContainsItems(expected)
        }
    }

    @Test
    fun `single team shown when 1 season for 1 year whilst current year in progress`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
            mockDriverOverviewTwoSeasonWithYearBetweenThemEndingInCurrentYear
        ) }

        val expected = listOf(
            DriverSummaryItem.RacedFor(
                currentSeasonYear,
                listOf(mockDriverOverviewConstructor),
                SINGLE,
                false
            ),
            DriverSummaryItem.RacedFor(
                2017,
                    listOf(mockDriverOverviewConstructor),
                SINGLE,
                true
            )
        )

        initSUT()

        sut.outputs.list.test {
            assertListContainsItems(expected)
        }
    }

    @Test
    fun `clicking open season opens season for driver`() = coroutineTest {

        val expected = 2020

        initSUT()

        sut.inputs.openSeason(expected)

        sut.outputs.openSeason.test {
            assertDataEventValue(Pair(mockDriverId, expected))
        }
    }

    @Test
    fun `clicking open url forwards open url event`() = coroutineTest {

        val expectUrl = "http://www.google.com"

        initSUT()

        sut.inputs.openUrl(expectUrl)

        sut.outputs.openUrl.test {
            assertDataEventValue(expectUrl)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "2019,2020,2021,START_END",
        "2018,2020,2022,SINGLE",
        "    ,2020,2021,END",
        "    ,2020,2022,SINGLE",
        "2018,2020,    ,SINGLE",
        "2019,2020,    ,START",
        "    ,2020,    ,SINGLE",
        "2019,2020,2022,START",
        "2019,2021,2022,END"
    )
    fun `getPipeType correct sequence of years returns the correct pipe type`(previous: Int?, current: Int, next: Int?, pipeType: PipeType) {

        initSUT()

        assertEquals(pipeType, sut.getPipeType(current, next, previous))
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