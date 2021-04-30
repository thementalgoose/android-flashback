package tmg.flashback.statistics.ui.overviews.constructor

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.statistics.ui.overview.constructor.summary.ConstructorSummaryItem
import tmg.flashback.statistics.ui.overview.driver.summary.PipeType
import tmg.flashback.data.db.stats.ConstructorRepository
import tmg.flashback.statistics.R
import tmg.flashback.statistics.testutils.*
import tmg.flashback.statistics.testutils.BaseTest
import tmg.flashback.statistics.testutils.assertListContainsItem
import tmg.flashback.statistics.testutils.assertListMatchesItem
import tmg.flashback.statistics.testutils.test
import tmg.flashback.statistics.ui.overview.constructor.ConstructorViewModel
import tmg.flashback.statistics.ui.overviews.*
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.flashback.statistics.ui.util.position

internal class ConstructorViewModelTest: BaseTest() {

    lateinit var sut: ConstructorViewModel

    private var mockConstructorRepository: ConstructorRepository = mockk(relaxed = true)
    private var mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {

        every { mockConnectivityManager.isConnected } returns true
    }

    private fun initSUT() {

        sut = ConstructorViewModel(mockConstructorRepository, mockConnectivityManager)
        sut.inputs.setup(mockConstructorId)
    }

    @Test
    fun `setup loads error state when network connectivity is down`() = coroutineTest {

        every { mockConnectivityManager.isConnected } returns false
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(null) }

        val expected = listOf(ConstructorSummaryItem.ErrorItem(SyncDataItem.NoNetwork))

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `setup loads an error state when constructor overview is returned as null`() = coroutineTest {

        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(null) }
        val expected = listOf(ConstructorSummaryItem.ErrorItem(SyncDataItem.Unavailable(
            DataUnavailable.CONSTRUCTOR_NOT_EXIST)))

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `setup which contains championship item in progress`() = coroutineTest {

        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(
            mockConstructorOverviewChampionshipInProgress
        ) }

        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem {
                it is ConstructorSummaryItem.ErrorItem &&
                        it.item is SyncDataItem.MessageRes &&
                        (it.item as SyncDataItem.MessageRes).msg == R.string.results_accurate_for_round
            }
        }
    }

    @Test
    fun `contains header item with appropriate mock data`() = coroutineTest {

        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(
            mockConstructorOverview
        ) }

        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem {
                if (it is ConstructorSummaryItem.Header) {
                    assertEquals(mockConstructorOverview.name, it.constructorName)
                    assertEquals(mockConstructorOverview.color, it.constructorColor)
                    assertEquals(mockConstructorOverview.nationality, it.constructorNationality)
                    assertEquals(mockConstructorOverview.nationalityISO, it.constructorNationalityISO)
                    assertEquals(mockConstructorOverview.wikiUrl, it.constructorWikiUrl)
                    return@assertListMatchesItem true
                }
                return@assertListMatchesItem false
            }
        }
    }

    @Test
    fun `list contains highlighted championship quantity result when constructor has championships`() = coroutineTest {

        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(
            mockConstructorOverviewChampionshipWon
        ) }

        initSUT()

        sut.outputs.list.test {
            assertListContainsItem(
                ConstructorSummaryItem.Stat(
                tint = R.attr.f1Favourite,
                icon = R.drawable.ic_menu_constructors,
                label = R.string.constructor_overview_stat_titles,
                value = "1"
            ))
        }
    }

    @Test
    fun `list contains highlighted championship quantity result when constructor has not won championships`() = coroutineTest {

        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(
            mockConstructorOverviewChampionshipNotWon
        ) }

        initSUT()

        sut.outputs.list.test {
            assertListContainsItem(
                ConstructorSummaryItem.Stat(
                icon = R.drawable.ic_menu_constructors,
                label = R.string.constructor_overview_stat_titles,
                value = "0"
            ))
        }
    }

    @Test
    fun `list doesn't contain career best championship if constructor is in progress`() = coroutineTest {

        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(
            mockConstructorOverviewChampionshipWonInProgress
        ) }

        initSUT()

        sut.outputs.list.test {
            assertListExcludesItem(
                ConstructorSummaryItem.Stat(
                icon = R.drawable.ic_championship_order,
                label = R.string.constructor_overview_stat_best_championship_position,
                value = "1st"
            ))
        }
    }

    @Test
    fun `list contains career best championship if constructor has completed a season`() = coroutineTest {

        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(
            mockConstructorOverview
        ) }

        initSUT()

        sut.outputs.list.test {
            assertListContainsItem(
                ConstructorSummaryItem.Stat(
                icon = R.drawable.ic_championship_order,
                label = R.string.constructor_overview_stat_best_championship_position,
                value = "1st"
            ))
        }
    }

    @Test
    fun `DriverViewModel setup loads standard list of statistics items`() = coroutineTest {

        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(
            mockConstructorOverview
        ) }

        val expected = listOf(
                expectedConstructorHeader,
                ConstructorSummaryItem.Stat(
                        icon = R.drawable.ic_race_grid,
                        label = R.string.constructor_overview_stat_races,
                        value = mockConstructorOverview.races.toString()
                ),
                ConstructorSummaryItem.Stat(
                        icon = R.drawable.ic_standings,
                        label = R.string.constructor_overview_stat_race_wins,
                        value = mockConstructorOverview.totalWins.toString()
                ),
                ConstructorSummaryItem.Stat(
                        icon = R.drawable.ic_podium,
                        label = R.string.constructor_overview_stat_race_podiums,
                        value = mockConstructorOverview.totalPodiums.toString()
                ),
                ConstructorSummaryItem.Stat(
                        icon = R.drawable.ic_status_finished,
                        label = R.string.constructor_overview_stat_best_finish,
                        value = mockConstructorOverview.bestFinish.position()
                ),
                ConstructorSummaryItem.Stat(
                        icon = R.drawable.ic_race_points,
                        label = R.string.constructor_overview_stat_points,
                        value = mockConstructorOverview.totalPoints.toString()
                ),
                ConstructorSummaryItem.Stat(
                        icon = R.drawable.ic_finishes_in_points,
                        label = R.string.constructor_overview_stat_points_finishes,
                        value = mockConstructorOverview.finishesInPoints.toString()
                ),
                ConstructorSummaryItem.Stat(
                        icon = R.drawable.ic_qualifying_pole,
                        label = R.string.constructor_overview_stat_qualifying_poles,
                        value = mockConstructorOverview.totalQualifyingPoles.toString()
                ),
                ConstructorSummaryItem.Stat(
                        icon = R.drawable.ic_qualifying_front_row,
                        label = R.string.constructor_overview_stat_qualifying_top_3,
                        value = mockConstructorOverview.totalQualifyingTop3.toString()
                )
        )

        initSUT()

        sut.outputs.list.test {
            assertListContainsItems(expected)
        }
    }

    @Test
    fun `team ordering and highlighting default setup`() = coroutineTest {

        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(
            mockConstructorOverviewStandings
        ) }
        val expected = listOf<ConstructorSummaryItem>(
                summaryHistory(
                        season = 2018,
                        type = PipeType.START
                ),
                summaryHistory(
                        season = 2017,
                        type = PipeType.END
                ),
                summaryHistory(
                        season = 2015,
                        type = PipeType.SINGLE
                ),
                summaryHistory(
                        season = 2013,
                        type = PipeType.START
                ),
                summaryHistory(
                        season = 2012,
                        type = PipeType.START_END
                ),
                summaryHistory(
                        season = 2011,
                        type = PipeType.START_END
                ),
                summaryHistory(
                        season = 2010,
                        type = PipeType.END
                ),
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
            assertDataEventValue(Pair(mockConstructorId, expected))
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

    private fun summaryHistory(type: PipeType, season: Int): ConstructorSummaryItem.History {
        return ConstructorSummaryItem.History(
                pipe = type,
                season = season,
                isInProgress = false,
                championshipPosition = mockConstructorOverviewStanding1.championshipStanding,
                points = mockConstructorOverviewStanding1.points,
                colour = mockConstructorOverview.color,
                drivers = mockConstructorOverviewStanding1.drivers.values.toList()
        )
    }

    private val expectedConstructorHeader: ConstructorSummaryItem.Header =
            ConstructorSummaryItem.Header(
                    constructorName = mockConstructorOverview.name,
                    constructorColor = mockConstructorOverview.color,
                    constructorNationality =  mockConstructorOverview.nationality,
                    constructorNationalityISO =  mockConstructorOverview.nationalityISO,
                    constructorWikiUrl = mockConstructorOverview.wikiUrl
            )
}