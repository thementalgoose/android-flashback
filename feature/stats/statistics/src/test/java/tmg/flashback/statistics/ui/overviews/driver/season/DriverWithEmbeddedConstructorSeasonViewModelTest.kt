package tmg.flashback.statistics.ui.overviews.driver.season

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.core.device.managers.NetworkConnectivityManager
import tmg.core.ui.controllers.ThemeController
import tmg.core.ui.model.AnimationSpeed
import tmg.flashback.statistics.ui.overview.driver.summary.PipeType.*
import tmg.flashback.formula1.model.DriverHistorySeasonRace
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.overview.driver.season.DriverSeasonItem
import tmg.flashback.statistics.ui.overview.driver.season.DriverSeasonViewModel
import tmg.flashback.statistics.ui.overviews.*
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.flashback.statistics.ui.util.position
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertListContainsItems
import tmg.testutils.livedata.test

//internal class DriverWithEmbeddedConstructorSeasonViewModelTest: BaseTest() {
//
//    lateinit var sut: DriverSeasonViewModel
//
//    private var mockDriverRepository: DriverRepository = mockk(relaxed = true)
//    private var mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
//    private var mockThemeController: ThemeController = mockk(relaxed = true)
//
//    @BeforeEach
//    internal fun setUp() {
//
//        every { mockConnectivityManager.isConnected } returns true
//        every { mockThemeController.animationSpeed } returns AnimationSpeed.NONE
//    }
//
//    private fun initSUT() {
//        sut = DriverSeasonViewModel(
//                mockDriverRepository,
//                mockConnectivityManager,
//                mockThemeController
//        )
//        sut.inputs.setup(mockDriverId, 2019)
//    }
//
//    @Test
//    fun `no network connection shows no network error`() = coroutineTest {
//
//        every { mockConnectivityManager.isConnected } returns false
//        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(null) }
//
//        val expected = listOf<DriverSeasonItem>(
//                DriverSeasonItem.ErrorItem(SyncDataItem.NoNetwork)
//        )
//
//        initSUT()
//
//        sut.outputs.list.test {
//            assertValue(expected)
//        }
//    }
//
//    @Test
//    fun `no driver overview found but valid network connections shows driver not exist error`() = coroutineTest {
//
//        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(null) }
//
//        val expected = listOf<DriverSeasonItem>(
//                DriverSeasonItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.DRIVER_NOT_EXIST))
//        )
//
//        initSUT()
//
//        sut.outputs.list.test {
//            assertValue(expected)
//        }
//    }
//
//    @Test
//    fun `init shows driver split when theres one constructor changes`() = coroutineTest {
//
//        val conditionedDriverOverview = mockDriverOverview.copy(
//                standings = listOf(
//                    mockDriverOverview2019Standing.copy(
//                        constructors = listOf(
//                                mockDriverOverviewConstructor
//                        )
//                ))
//        )
//
//        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(conditionedDriverOverview) }
//
//        val expected = listOf(
//                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor, SINGLE, false)
//        )
//
//        initSUT()
//
//        sut.outputs.list.test {
//            assertListContainsItems(expected)
//        }
//    }
//
//    @Test
//    fun `init shows driver split when theres two constructor changes`() = coroutineTest {
//
//        val conditionedDriverOverview = mockDriverOverview.copy(
//                standings = listOf(
//                    mockDriverOverview2019Standing.copy(
//                        constructors = listOf(
//                                mockDriverOverviewConstructor,
//                                mockDriverOverviewConstructor2
//                        )
//                ))
//        )
//
//        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(conditionedDriverOverview) }
//
//        val expected = listOf(
//                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor2, START, false),
//                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor, END, false)
//        )
//
//        initSUT()
//
//        sut.outputs.list.test {
//            assertListContainsItems(expected)
//        }
//    }
//
//    @Test
//    fun `init shows driver split when theres three constructor changes`() = coroutineTest {
//
//        val conditionedDriverOverview = mockDriverOverview.copy(
//                standings = listOf(
//                    mockDriverOverview2019Standing.copy(
//                        constructors = listOf(
//                                mockDriverOverviewConstructor,
//                                mockDriverOverviewConstructor3,
//                                mockDriverOverviewConstructor2
//                        )
//                ))
//        )
//
//        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(conditionedDriverOverview) }
//
//        val expected = listOf(
//                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor2, START, false),
//                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor3, SINGLE_PIPE, false),
//                DriverSeasonItem.RacedFor(null, mockDriverOverviewConstructor, END, false)
//        )
//
//        initSUT()
//
//        sut.outputs.list.test {
//            assertListContainsItems(expected)
//        }
//    }
//
//    @Test
//    fun `init for season contains season summary`() = coroutineTest {
//
//        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
//            mockDriverOverview
//        ) }
//
//        val expected = listOf(
//                DriverSeasonItem.Stat(
//                        icon = R.drawable.ic_team,
//                        label = R.string.driver_overview_stat_career_team,
//                        value = ""
//                ),
//                DriverSeasonItem.Stat(
//                        icon = R.drawable.ic_standings,
//                        label = R.string.driver_overview_stat_career_wins,
//                        value = mockDriverOverview2019Standing.wins.toString()
//                ),
//                DriverSeasonItem.Stat(
//                        icon = R.drawable.ic_podium,
//                        label = R.string.driver_overview_stat_career_podiums,
//                        value = mockDriverOverview2019Standing.podiums.toString()
//                ),
//                DriverSeasonItem.Stat(
//                        icon = R.drawable.ic_race_starts,
//                        label = R.string.driver_overview_stat_race_starts,
//                        value = mockDriverOverview2019Standing.raceStarts.toString()
//                ),
//                DriverSeasonItem.Stat(
//                        icon = R.drawable.ic_race_finishes,
//                        label = R.string.driver_overview_stat_race_finishes,
//                        value = mockDriverOverview2019Standing.raceFinishes.toString()
//                ),
//                DriverSeasonItem.Stat(
//                        icon = R.drawable.ic_race_retirements,
//                        label = R.string.driver_overview_stat_race_retirements,
//                        value = mockDriverOverview2019Standing.raceRetirements.toString()
//                ),
//                DriverSeasonItem.Stat(
//                        icon = R.drawable.ic_best_finish,
//                        label = R.string.driver_overview_stat_career_best_finish,
//                        value = mockDriverOverview2019Standing.bestFinish.position()
//                ),
//                DriverSeasonItem.Stat(
//                        icon = R.drawable.ic_finishes_in_points,
//                        label = R.string.driver_overview_stat_career_points_finishes,
//                        value = mockDriverOverview2019Standing.finishesInPoints.toString()
//                ),
//                DriverSeasonItem.Stat(
//                        icon = R.drawable.ic_race_points,
//                        label = R.string.driver_overview_stat_career_points,
//                        value = mockDriverOverview2019Standing.points.pointsDisplay()
//                ),
//                DriverSeasonItem.Stat(
//                        icon = R.drawable.ic_qualifying_pole,
//                        label = R.string.driver_overview_stat_career_qualifying_pole,
//                        value = mockDriverOverview2019Standing.qualifyingPoles.toString()
//                ),
//                DriverSeasonItem.Stat(
//                        icon = R.drawable.ic_qualifying_front_row,
//                        label = R.string.driver_overview_stat_career_qualifying_top_3,
//                        value = mockDriverOverview2019Standing.qualifyingTop3.toString()
//                ),
//                DriverSeasonItem.Stat(
//                        icon = R.drawable.ic_qualifying_top_ten,
//                        label = R.string.driver_overview_stat_career_qualifying_top_10,
//                        value = mockDriverOverview2019Standing.totalQualifyingAbove(10).toString()
//                )
//        )
//
//        initSUT()
//
//        sut.outputs.list.test {
//            assertListContainsItems(expected)
//        }
//    }
//
//
//    @Test
//    fun `init contains result header `() = coroutineTest {
//
//        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
//            mockDriverOverview
//        ) }
//
//        val expected = listOf(
//                DriverSeasonItem.ResultHeader
//        )
//
//        initSUT()
//
//        sut.outputs.list.test {
//            assertListContainsItems(expected)
//        }
//    }
//
//
//    @Test
//    fun `init contains race overview summary cards`() = coroutineTest {
//
//        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
//            mockDriverOverview
//        ) }
//
//        val expected = listOf(
//                DriverSeasonItem.ResultHeader
//        )
//
//        initSUT()
//
//        sut.outputs.list.test {
//            assertListContainsItems(expected)
//        }
//    }
//
//
//
//    @Test
//    fun `init contains rounds that summarise driver overview`() = coroutineTest {
//
//        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
//            mockDriverOverview
//        ) }
//
//        val expected = listOf<DriverSeasonItem>(
//                expectedFirstRound,
//                expectedSecondRound
//        )
//
//        initSUT()
//
//        sut.outputs.list.test {
//            assertListContainsItems(expected)
//        }
//    }
//
//
//    @Test
//    fun `clicking season round fires go to season round event`() = coroutineTest {
//
//        initSUT()
//
//        sut.inputs.clickSeasonRound(expectedFirstRound)
//
//        sut.outputs.openSeasonRound.test {
//            assertDataEventValue(expectedFirstRound)
//        }
//    }
//
//    private val expectedFirstRound: DriverSeasonItem.Result
//        get() = convertDriverOverviewRace(mockDriverOverviewRaceFirst)
//
//    private val expectedSecondRound: DriverSeasonItem.Result
//        get() = convertDriverOverviewRace(mockDriverOverviewRaceSecond)
//
//    private fun convertDriverOverviewRace(race: DriverHistorySeasonRace): DriverSeasonItem.Result {
//        return DriverSeasonItem.Result(
//                season = race.season,
//                round = race.round,
//                raceName = race.raceName,
//                circuitName = race.circuitName,
//                circuitId = race.circuitId,
//                raceCountry = race.circuitNationality,
//                raceCountryISO = race.circuitNationalityISO,
//                showConstructorLabel = false,
//                constructor = mockDriverOverviewConstructor,
//                date = race.date,
//                qualified = race.qualified,
//                finished = race.finished,
//                raceStatus = race.status,
//                points = race.points,
//                maxPoints = 25,
//                animationSpeed = AnimationSpeed.NONE
//        )
//    }
//}
