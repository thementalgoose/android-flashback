package tmg.flashback.ui.overviews.driver.season

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.R
import tmg.flashback.controllers.AppearanceController
import tmg.flashback.ui.overviews.*
import tmg.flashback.ui.overviews.driver.summary.PipeType.*
import tmg.flashback.managers.networkconnectivity.NetworkConnectivityManager
import tmg.flashback.data.db.stats.DriverRepository
import tmg.flashback.data.enums.BarAnimation
import tmg.flashback.data.models.stats.DriverOverviewRace
import tmg.flashback.ui.shared.sync.SyncDataItem
import tmg.flashback.ui.shared.viewholders.DataUnavailable.DRIVER_NOT_EXIST
import tmg.flashback.testutils.*
import tmg.flashback.ui.utils.position

internal class DriverSeasonViewModelTest: BaseTest() {

    lateinit var sut: DriverSeasonViewModel

    private var mockDriverRepository: DriverRepository = mockk(relaxed = true)
    private var mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private var mockAppearanceController: AppearanceController = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {

        every { mockConnectivityManager.isConnected } returns true
        every { mockAppearanceController.barAnimation } returns BarAnimation.NONE
    }

    private fun initSUT() {
        sut = DriverSeasonViewModel(
                mockDriverRepository,
                mockConnectivityManager,
                mockAppearanceController
        )
        sut.inputs.setup(mockDriverId, 2019)
    }

    @Test
    fun `DriverSeasonViewModel no network connection shows no network error`() = coroutineTest {

        every { mockConnectivityManager.isConnected } returns false
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(null) }

        val expected = listOf<DriverSeasonItem>(
                DriverSeasonItem.ErrorItem(SyncDataItem.NoNetwork)
        )

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `DriverSeasonViewModel no driver overview found but valid network connections shows driver not exist error`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(null) }

        val expected = listOf<DriverSeasonItem>(
                DriverSeasonItem.ErrorItem(SyncDataItem.Unavailable(DRIVER_NOT_EXIST))
        )

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
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

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(conditionedDriverOverview) }

        val expected = listOf(
                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor, SINGLE, false)
        )

        initSUT()

        sut.outputs.list.test {
            assertListContainsItems(expected)
        }
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

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(conditionedDriverOverview) }

        val expected = listOf(
                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor2, START, false),
                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor, END, false)
        )

        initSUT()

        sut.outputs.list.test {
            assertListContainsItems(expected)
        }
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

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(conditionedDriverOverview) }

        val expected = listOf(
                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor2, START, false),
                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor3, SINGLE_PIPE, false),
                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor, END, false)
        )

        initSUT()

        sut.outputs.list.test {
            assertListContainsItems(expected)
        }
    }

    @Test
    fun `DriverSeasonViewModel init for season contains season summary`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(mockDriverOverview) }

        val expected = listOf(
                DriverSeasonItem.Stat(
                        icon = R.drawable.ic_team,
                        label = R.string.driver_overview_stat_career_team,
                        value = ""
                ),
                DriverSeasonItem.Stat(
                        icon = R.drawable.ic_standings,
                        label = R.string.driver_overview_stat_career_wins,
                        value = mockDriverOverview2019Standing.wins.toString()
                ),
                DriverSeasonItem.Stat(
                        icon = R.drawable.ic_podium,
                        label = R.string.driver_overview_stat_career_podiums,
                        value = mockDriverOverview2019Standing.podiums.toString()
                ),
                DriverSeasonItem.Stat(
                        icon = R.drawable.ic_race_starts,
                        label = R.string.driver_overview_stat_race_starts,
                        value = mockDriverOverview2019Standing.raceStarts.toString()
                ),
                DriverSeasonItem.Stat(
                        icon = R.drawable.ic_race_finishes,
                        label = R.string.driver_overview_stat_race_finishes,
                        value = mockDriverOverview2019Standing.raceFinishes.toString()
                ),
                DriverSeasonItem.Stat(
                        icon = R.drawable.ic_race_retirements,
                        label = R.string.driver_overview_stat_race_retirements,
                        value = mockDriverOverview2019Standing.raceRetirements.toString()
                ),
                DriverSeasonItem.Stat(
                        icon = R.drawable.ic_best_finish,
                        label = R.string.driver_overview_stat_career_best_finish,
                        value = mockDriverOverview2019Standing.bestFinish.position()
                ),
                DriverSeasonItem.Stat(
                        icon = R.drawable.ic_finishes_in_points,
                        label = R.string.driver_overview_stat_career_points_finishes,
                        value = mockDriverOverview2019Standing.finishesInPoints.toString()
                ),
                DriverSeasonItem.Stat(
                        icon = R.drawable.ic_race_points,
                        label = R.string.driver_overview_stat_career_points,
                        value = mockDriverOverview2019Standing.points.toString()
                ),
                DriverSeasonItem.Stat(
                        icon = R.drawable.ic_qualifying_pole,
                        label = R.string.driver_overview_stat_career_qualifying_pole,
                        value = mockDriverOverview2019Standing.qualifyingPoles.toString()
                ),
                DriverSeasonItem.Stat(
                        icon = R.drawable.ic_qualifying_front_row,
                        label = R.string.driver_overview_stat_career_qualifying_top_3,
                        value = mockDriverOverview2019Standing.qualifyingTop3.toString()
                ),
                DriverSeasonItem.Stat(
                        icon = R.drawable.ic_qualifying_top_ten,
                        label = R.string.driver_overview_stat_career_qualifying_top_10,
                        value = mockDriverOverview2019Standing.totalQualifyingAbove(10).toString()
                )
        )

        initSUT()

        sut.outputs.list.test {
            assertListContainsItems(expected)
        }
    }


    @Test
    fun `DriverSeasonViewModel init contains result header `() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(mockDriverOverview) }

        val expected = listOf(
                DriverSeasonItem.ResultHeader
        )

        initSUT()

        sut.outputs.list.test {
            assertListContainsItems(expected)
        }
    }


    @Test
    fun `DriverSeasonViewModel init contains race overview summary cards`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(mockDriverOverview) }

        val expected = listOf(
                DriverSeasonItem.ResultHeader
        )

        initSUT()

        sut.outputs.list.test {
            assertListContainsItems(expected)
        }
    }



    @Test
    fun `DriverSeasonViewModel init contains rounds that summarise driver overview`() = coroutineTest {

        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(mockDriverOverview) }

        val expected = listOf<DriverSeasonItem>(
                expectedFirstRound,
                expectedSecondRound
        )

        initSUT()

        sut.outputs.list.test {
            assertListContainsItems(expected)
        }
    }


    @Test
    fun `DriverSeasonViewModel clicking season round fires go to season round event`() = coroutineTest {

        initSUT()

        sut.inputs.clickSeasonRound(expectedFirstRound)

        sut.outputs.openSeasonRound.test {
            assertDataEventValue(expectedFirstRound)
        }
    }

    private val expectedFirstRound: DriverSeasonItem.Result
        get() = convertDriverOverviewRace(mockDriverOverviewRaceFirst)

    private val expectedSecondRound: DriverSeasonItem.Result
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
                showConstructorLabel = false,
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
