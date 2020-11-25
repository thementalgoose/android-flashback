package tmg.flashback.overviews.constructor

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
import tmg.flashback.overviews.*
import tmg.flashback.overviews.constructor.summary.ConstructorSummaryItem
import tmg.flashback.overviews.driver.summary.PipeType
import tmg.flashback.repo.NetworkConnectivityManager
import tmg.flashback.repo.db.stats.ConstructorDB
import tmg.flashback.rss.testutils.BaseTest
import tmg.flashback.shared.sync.SyncDataItem
import tmg.flashback.shared.viewholders.DataUnavailable
import tmg.flashback.testutils.*
import tmg.flashback.utils.position

@FlowPreview
@ExperimentalCoroutinesApi
class ConstructorViewModelTest: BaseTest() {

    lateinit var sut: ConstructorViewModel

    private var mockConstructorDB: ConstructorDB = mock()
    private var mockConnectivityManager: NetworkConnectivityManager = mock()

    @BeforeEach
    internal fun setUp() {

        whenever(mockConnectivityManager.isConnected).thenReturn(true)
    }

    private fun initSUT() {

        sut = ConstructorViewModel(mockConstructorDB, mockConnectivityManager, testScopeProvider)
        sut.inputs.setup(mockConstructorId)
    }

    @Test
    fun `ConstructorViewModel setup loads error state when network connectivity is down`() = coroutineTest {

        whenever(mockConnectivityManager.isConnected).thenReturn(false)
        whenever(mockConstructorDB.getConstructorOverview(any())).thenReturn(flow { emit(null) })
        val expected = listOf(ConstructorSummaryItem.ErrorItem(SyncDataItem.NoNetwork))

        initSUT()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `ConstructorViewModel setup loads an error state when constructor overview is returned as null`() = coroutineTest {

        whenever(mockConstructorDB.getConstructorOverview(any())).thenReturn(flow { emit(null) })
        val expected = listOf(ConstructorSummaryItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.CONSTRUCTOR_NOT_EXIST)))

        initSUT()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `ConstructorViewModel setup which contains championship item in progress`() = coroutineTest {

        whenever(mockConstructorDB.getConstructorOverview(any())).thenReturn(flow { emit(mockConstructorOverviewChampionshipInProgress) })

        initSUT()

        assertListContains(sut.outputs.list) {
            it is ConstructorSummaryItem.ErrorItem &&
                    it.item is SyncDataItem.MessageRes &&
                    (it.item as SyncDataItem.MessageRes).msg == R.string.results_accurate_for_round
        }
    }

    @Test
    fun `ConstructorViewModel contains header item with appropriate mock data`() = coroutineTest {

        whenever(mockConstructorDB.getConstructorOverview(any())).thenReturn(flow { emit(mockConstructorOverview) })

        initSUT()

        assertListContains(sut.outputs.list) {
            if (it is ConstructorSummaryItem.Header) {
                assertEquals(mockConstructorOverview.name, it.constructorName)
                assertEquals(mockConstructorOverview.color, it.constructorColor)
                assertEquals(mockConstructorOverview.nationality, it.constructorNationality)
                assertEquals(mockConstructorOverview.nationalityISO, it.constructorNationalityISO)
                assertEquals(mockConstructorOverview.wikiUrl, it.constructorWikiUrl)
                return@assertListContains true
            }
            return@assertListContains false
        }
    }

    @Test
    fun `ConstructorViewModel list contains highlighted championship quantity result when constructor has championships`() = coroutineTest {

        whenever(mockConstructorDB.getConstructorOverview(any())).thenReturn(flow { emit(mockConstructorOverviewChampionshipWon) })

        initSUT()

        assertListContains(sut.outputs.list) {
            it == ConstructorSummaryItem.Stat(
                    tint = R.attr.f1Favourite,
                    icon = R.drawable.ic_menu_constructors,
                    label = R.string.constructor_overview_stat_titles,
                    value = "1"
            )
        }
    }

    @Test
    fun `ConstructorViewModel list contains highlighted championship quantity result when constructor has not won championships`() = coroutineTest {

        whenever(mockConstructorDB.getConstructorOverview(any())).thenReturn(flow { emit(mockConstructorOverviewChampionshipNotWon) })

        initSUT()

        assertListContains(sut.outputs.list) {
            it == ConstructorSummaryItem.Stat(
                    icon = R.drawable.ic_menu_constructors,
                    label = R.string.constructor_overview_stat_titles,
                    value = "0"
            )
        }
    }

    @Test
    fun `ConstructorViewModel list doesn't contain career best championship if constructor is in progress`() = coroutineTest {

        whenever(mockConstructorDB.getConstructorOverview(any())).thenReturn(flow { emit(mockConstructorOverviewChampionshipWonInProgress) })

        initSUT()

        assertListDoesntContains(sut.outputs.list) {
            it == ConstructorSummaryItem.Stat(
                    icon = R.drawable.ic_championship_order,
                    label = R.string.constructor_overview_stat_best_championship_position,
                    value = "1st"
            )
        }
    }

    @Test
    fun `ConstructorViewModel list contains career best championship if constructor has completed a season`() = coroutineTest {

        whenever(mockConstructorDB.getConstructorOverview(any())).thenReturn(flow { emit(mockConstructorOverview) })

        initSUT()

        assertListContains(sut.outputs.list) {
            it == ConstructorSummaryItem.Stat(
                    icon = R.drawable.ic_championship_order,
                    label = R.string.constructor_overview_stat_best_championship_position,
                    value = "1st"
            )
        }
    }

    @Test
    fun `DriverViewModel setup loads standard list of statistics items`() = coroutineTest {

        whenever(mockConstructorDB.getConstructorOverview(any())).thenReturn(flow { emit(mockConstructorOverview) })
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

        assertListContainsValues(sut.outputs.list, expected)
    }

    @Test
    fun `ConstructorViewModel team ordering and highlighting default setup`() = coroutineTest {

        whenever(mockConstructorDB.getConstructorOverview(any())).thenReturn(flow { emit(mockConstructorOverviewStandings) })
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

        assertListContainsValues(sut.outputs.list, expected)
    }


    @Test
    fun `ConstructorViewModel clicking open season opens season for driver`() = coroutineTest {

        val expected = 2020

        initSUT()

        sut.inputs.openSeason(expected)

        assertDataEventValue(Pair(mockConstructorId, expected), sut.outputs.openSeason)
    }

    @Test
    fun `ConstructorViewModel clicking open url forwards open url event`() = coroutineTest {

        val expectUrl = "http://www.google.com"

        initSUT()

        sut.inputs.openUrl(expectUrl)

        assertDataEventValue(expectUrl, sut.outputs.openUrl)
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockConstructorDB, mockConnectivityManager)
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