package tmg.flashback.statistics.ui.race

import androidx.lifecycle.map
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.ui.controllers.ThemeController
import tmg.flashback.ui.model.AnimationSpeed
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.controllers.RaceController
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.statistics.ui.race.RaceAdapterType.*
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test

internal class RaceViewModelTest: BaseTest() {

    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)
    private val mockRaceController: RaceController = mockk(relaxed = true)
    private val mockThemeController: ThemeController = mockk(relaxed = true)
    private val mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)

    private lateinit var sut: RaceViewModel

    private fun initSUT() {
        sut = RaceViewModel(
            mockRaceRepository,
            mockRaceController,
            mockThemeController,
            mockConnectivityManager,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockConnectivityManager.isConnected } returns true
        coEvery { mockRaceRepository.shouldSyncRace(any(), any()) } returns false
        coEvery { mockRaceRepository.getRace(any(), any()) } returns flow { emit(raceModel) }
        coEvery { mockRaceRepository.fetchRaces(any()) } returns true
        every { mockThemeController.animationSpeed } returns AnimationSpeed.QUICK
    }

    //region Race loading states

    @Test
    fun `race data with null value and no internet shows pull to refresh`() = coroutineTest {
        every { mockRaceRepository.getRace(any(), any()) } returns flow { emit(null) }
        every { mockConnectivityManager.isConnected } returns false

        initSUT()
        runBlockingTest {
            sut.inputs.initialise(2020, 1)
            sut.inputs.orderBy(RACE)
        }
        sut.outputs.raceItems.test {
            assertValue(Pair(RACE, listOf(
                RaceModel.errorItemModel(SyncDataItem.PullRefresh)
            )))
        }
    }

    @Test
    fun `race data with null value and internet shows race data empty`() = coroutineTest {
        every { mockRaceRepository.getRace(any(), any()) } returns flow { emit(null) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.initialise(2020, 1)
        sut.outputs.raceItems.test {
            assertValue(Pair(RACE, listOf(
                RaceModel.errorItemModel(SyncDataItem.Unavailable(DataUnavailable.RACE_DATA_EMPTY))
            )))
        }
    }

    @Test
    fun `race data with no data and date in the future shows race in future`() = coroutineTest {
        every { mockRaceRepository.getRace(any(), any()) } returns flow { emit(
            Race.model(
                raceInfo = RaceInfo.model(
                    date = LocalDate.now().plusDays(1)
                ),
                q1 = emptyMap(),
                q2 = emptyMap(),
                q3 = emptyMap(),
                qSprint = emptyMap(),
                race = emptyMap()
            )
        ) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.initialise(2020, 1)
        sut.outputs.raceItems.test {
            assertValue(Pair(RACE, listOf(
                RaceModel.errorItemModel(SyncDataItem.Unavailable(DataUnavailable.RACE_IN_FUTURE))
            )))
        }
    }

    @Test
    fun `race data with no data and date today shows coming soon`() = coroutineTest {
        every { mockRaceRepository.getRace(any(), any()) } returns flow { emit(
            Race.model(
                raceInfo = RaceInfo.model(
                    date = LocalDate.now()
                ),
                q1 = emptyMap(),
                q2 = emptyMap(),
                q3 = emptyMap(),
                qSprint = emptyMap(),
                race = emptyMap()
            )
        ) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.initialise(2020, 1)
        sut.outputs.raceItems.test {
            assertValue(Pair(RACE, listOf(
                RaceModel.errorItemModel(SyncDataItem.Unavailable(DataUnavailable.COMING_SOON_RACE))
            )))
        }
    }

    @Test
    fun `race data with no data and date within 10 days shows coming soon`() = coroutineTest {
        every { mockRaceRepository.getRace(any(), any()) } returns flow { emit(
            Race.model(
                raceInfo = RaceInfo.model(
                    date = LocalDate.now().minusDays(1L)
                ),
                q1 = emptyMap(),
                q2 = emptyMap(),
                q3 = emptyMap(),
                qSprint = emptyMap(),
                race = emptyMap()
            )
        ) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.initialise(2020, 1)
        sut.outputs.raceItems.test {
            assertValue(Pair(RACE, listOf(
                RaceModel.errorItemModel(SyncDataItem.Unavailable(DataUnavailable.COMING_SOON_RACE))
            )))
        }
    }

    @Test
    fun `race data with no data and date more than 10 days shows race data empty`() = coroutineTest {
        every { mockRaceRepository.getRace(any(), any()) } returns flow { emit(
            Race.model(
                raceInfo = RaceInfo.model(
                    date = LocalDate.now().minusDays(11L)
                ),
                q1 = emptyMap(),
                q2 = emptyMap(),
                q3 = emptyMap(),
                qSprint = emptyMap(),
                race = emptyMap()
            )
        ) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.initialise(2020, 1)
        sut.outputs.raceItems.test {
            assertValue(Pair(RACE, listOf(
                RaceModel.errorItemModel(SyncDataItem.Unavailable(DataUnavailable.RACE_DATA_EMPTY))
            )))
        }
    }

    //endregion

    //region Race display types

    @Test
    fun `race type displays race item`() = coroutineTest {
        initSUT()
        runBlockingTest {
            sut.inputs.initialise(2020, 1)
        }

        sut.outputs.raceItems
            .map { it.second }
            .test {
                latestValue!!.forEachIndexed { index, model ->
                    when (model) {
                        is RaceModel.Overview -> assertEquals(0, index)
                        is RaceModel.Podium -> {
                            assertEquals(1, index)
                            assertEquals(driver1, model.driverFirst.driver)
                            assertEquals(driver2, model.driverSecond.driver)
                            assertEquals(driver4, model.driverThird.driver)
                        }
                        is RaceModel.RaceHeader -> assertEquals(2, index)
                        is RaceModel.Single -> {
                            assertEquals(3, index)
                            assertEquals(driver3, model.driver)
                        }
                        else -> fail("Unexpected value")
                    }
                }
            }
    }

    @Test
    fun `qualifying type displays race item`() = coroutineTest {
        initSUT()
        runBlockingTest {
            sut.inputs.initialise(2020, 1)
            sut.inputs.orderBy(QUALIFYING_POS)
        }

        sut.outputs.raceItems
            .map { it.second }
            .test {
                latestValue!!.forEachIndexed { index, model ->
                    when (model) {
                        is RaceModel.Overview -> assertEquals(0, index)
                        is RaceModel.QualifyingHeader -> assertEquals(1, index)
                        is RaceModel.Single -> {
                            when (index) {
                                2 -> assertEquals(driver2, model.driver)
                                3 -> assertEquals(driver1, model.driver)
                                4 -> assertEquals(driver3, model.driver)
                                5 -> assertEquals(driver4, model.driver)
                                else -> fail("Unexpected value")
                            }
                        }
                        else -> fail("Unexpected value")
                    }
                }
            }

        runBlockingTest {
            sut.inputs.orderBy(QUALIFYING_POS_1)
        }

        sut.outputs.raceItems
            .map { it.second }
            .test {
                println(latestValue)
                latestValue!!.forEachIndexed { index, model ->
                    when (model) {
                        is RaceModel.Overview -> assertEquals(0, index)
                        is RaceModel.QualifyingHeader -> assertEquals(1, index)
                        is RaceModel.Single -> {
                            when (index) {
                                2 -> assertEquals(driver2, model.driver)
                                3 -> assertEquals(driver1, model.driver)
                                4 -> assertEquals(driver3, model.driver)
                                5 -> assertEquals(driver4, model.driver)
                                else -> fail("Unexpected value")
                            }
                        }
                        else -> fail("Unexpected value")
                    }
                }
            }

        runBlockingTest {
            sut.inputs.orderBy(QUALIFYING_POS_2)
        }

        sut.outputs.raceItems
            .map { it.second }
            .test {
                latestValue!!.forEachIndexed { index, model ->
                    when (model) {
                        is RaceModel.Overview -> assertEquals(0, index)
                        is RaceModel.QualifyingHeader -> assertEquals(1, index)
                        is RaceModel.Single -> {
                            when (index) {
                                2 -> assertEquals(driver2, model.driver)
                                3 -> assertEquals(driver1, model.driver)
                                4 -> assertEquals(driver3, model.driver)
                                5 -> assertEquals(driver4, model.driver)
                                else -> fail("Unexpected value")
                            }
                        }
                        else -> fail("Unexpected value")
                    }
                }
            }

    }

    @Test
    fun `constructors type displays race item`() = coroutineTest {
        initSUT()

        runBlockingTest {
            sut.inputs.initialise(2020, 1)
            sut.inputs.orderBy(CONSTRUCTOR_STANDINGS)
        }

        sut.outputs.raceItems.test {
            assertValue(Pair(CONSTRUCTOR_STANDINGS, listOf(
                RaceModel.overviewModel(),
                RaceModel.constructorStandingsModel(
                    points = 25.0 + 18.0 + 15.0 + 12.0,
                    driver = listOf(
                        driver1.driver to 25.0,
                        driver2.driver to 18.0,
                        driver4.driver to 15.0,
                        driver3.driver to 12.0,
                    )
                )
            )))
        }
    }

    //endregion

    //region Request

    @Test
    fun `race requests is not made when should sync is false`() = coroutineTest {
        coEvery { mockRaceRepository.shouldSyncRace(any(), any()) } returns false

        initSUT()
        runBlockingTest {
            sut.inputs.initialise(2020, 1)
            sut.inputs.orderBy(RACE)
        }

        coVerify(exactly = 0) {
            mockRaceRepository.fetchRaces(any())
        }
    }

    @Test
    fun `race requests is made when should sync is true`() = coroutineTest {
        coEvery { mockRaceRepository.shouldSyncRace(any(), any()) } returns true

        initSUT()
        sut.inputs.initialise(2020, 1)

        sut.outputs.raceItems
            .map { it.second }
            .test {
                assertListMatchesItem { it is RaceModel.RaceHeader }
            }
        coVerify {
            mockRaceRepository.fetchRaces(any())
        }
    }

    //endregion

    //region Go to driver

    @Test
    fun `go to driver fires go to driver event`() = coroutineTest {
        val id: String = "driverId"
        val name: String = "name"

        initSUT()
        sut.inputs.goToDriver(id, name)
        sut.outputs.goToDriverOverview.test {
            assertDataEventValue(Pair(id, name))
        }
    }

    //endregion

    //region Go to constructor

    @Test
    fun `go to constructor fires go to constructor event`() = coroutineTest {
    val id: String = "constructorId"
        val name: String = "name"

        initSUT()
        sut.inputs.goToConstructor(id, name)
        sut.outputs.goToConstructorOverview.test {
            assertDataEventValue(Pair(id, name))
        }
    }

    //endregion

    //region Qualifying delta toggle

    @Test
    fun `toggle qualifying delta`() = coroutineTest {
        initSUT()
        runBlockingTest {
            sut.initialise(2020, 1)
            sut.inputs.orderBy(QUALIFYING_POS)
        }

        sut.outputs.raceItems
            .map { it.second }
            .test {
                assertListMatchesItem { it is RaceModel.QualifyingHeader && !it.displayPrefs.deltas }
            }

        sut.inputs.toggleQualifyingDelta(true)

        sut.outputs.raceItems
            .map { it.second }
            .test {
                assertListMatchesItem { it is RaceModel.QualifyingHeader && it.displayPrefs.deltas }
            }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh calls overview and race repos and show loading false when done`() = coroutineTest {
        initSUT()
        sut.inputs.initialise(2020, 1)

        runBlockingTest {
            sut.inputs.refresh()
        }

        coVerify {
            mockRaceRepository.fetchRaces(any())
        }
        sut.outputs.showLoading.test {
            assertValue(false)
        }
    }

    //endregion
}

//internal class RaceViewModelTest: BaseTest() {
//
//    lateinit var sut: RaceViewModel
//
//    private val mockSeasonOverviewRepository: SeasonOverviewRepository = mockk(relaxed = true)
//    private val mockThemeController: ThemeController = mockk(relaxed = true)
//    private val mockRaceController: RaceController = mockk(relaxed = true)
//    private val mockConnectivityManager: tmg.core.device.managers.NetworkConnectivityManager = mockk(relaxed = true)
//
//    private val expectedSeasonRound: SeasonRound = SeasonRound(2019, 1)
//
//    @BeforeEach
//    internal fun setUp() {
//
//        every { mockConnectivityManager.isConnected } returns true
//        every { mockThemeController.animationSpeed } returns AnimationSpeed.NONE
//        every { mockRaceController.fadeDNF } returns true
//    }
//
//    private fun initSUT(roundDate: LocalDate? = null, orderBy: RaceAdapterType = RACE) {
//        sut = RaceViewModel(mockSeasonOverviewRepository, mockRaceController, mockThemeController, mockConnectivityManager)
//        val (season, round) = expectedSeasonRound
//        sut.inputs.initialise(season, round, roundDate)
//        sut.inputs.orderBy(orderBy)
//    }
//
//    //region Network
//
//    @Test
//    fun `init no network error shown when network isnt available`() = coroutineTest {
//
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(null) }
//        every { mockConnectivityManager.isConnected } returns false
//
//        initSUT()
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(
//                RACE,
//                listOf(RaceModel.ErrorItem(SyncDataItem.NoNetwork)),
//                expectedSeasonRound
//            ))
//        }
//    }
//
//    //endregion
//
//    @Test
//    fun `when round data is null and date supplied is in the future, show race in future unavailable message`() = coroutineTest {
//
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(null) }
//
//        initSUT(LocalDate.now().plusDays(1L))
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(
//                RACE,
//                listOf(RaceModel.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.RACE_IN_FUTURE))),
//                expectedSeasonRound
//            ))
//        }
//    }
//
//    @Test
//    fun `when round data is null and round date is in the past, show coming soon race data unavailable message`() = coroutineTest {
//
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(null) }
//
//        initSUT(LocalDate.now().minusDays(1L))
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(RACE, listOf(
//                RaceModel.ErrorItem(
//                    SyncDataItem.Unavailable(
//                        DataUnavailable.COMING_SOON_RACE
//                    )
//                )
//            ), expectedSeasonRound))
//        }
//    }
//
//    @Test
//    fun `when round data is null and date supplied is null, show missing race data unavailable message`() = coroutineTest {
//
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(null) }
//
//        initSUT(null)
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(RACE, listOf(
//                RaceModel.ErrorItem(
//                    SyncDataItem.Unavailable(
//                        DataUnavailable.RACE_DATA_EMPTY
//                    )
//                )
//            ), expectedSeasonRound))
//        }
//    }
//
//    @Test
//    fun `when round data is null and the round happened within the past 10 days, show the race is coming soon message`() = coroutineTest {
//
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(null) }
//        val showComingSoonMessageForNextDays = 5
//
//        initSUT(LocalDate.now().minusDays(showComingSoonMessageForNextDays - 1L))
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(RACE, listOf(
//                RaceModel.ErrorItem(
//                    SyncDataItem.Unavailable(
//                        DataUnavailable.COMING_SOON_RACE
//                    )
//                )
//            ), expectedSeasonRound))
//        }
//    }
//
//    @Test
//    fun `when round data is null and the round is happening or happened today, show the race is coming soon message`() = coroutineTest {
//
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(null) }
//
//        initSUT(LocalDate.now())
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(RACE, listOf(
//                RaceModel.ErrorItem(
//                    SyncDataItem.Unavailable(
//                        DataUnavailable.COMING_SOON_RACE
//                    )
//                )
//            ), expectedSeasonRound))
//        }
//    }
//
//    @Test
//    fun `when view type is (happy) constructor, standings show constructor standings items with list of drivers`() = coroutineTest {
//
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(MOCK_RACE_1) }
//
//        val expected = listOf<RaceModel>(
//            MOCK_RACE_1.getRaceOvevriew(),
//            RaceModel.ConstructorStandings(
//                mockConstructorBeta, 30.0, listOf(
//                    Pair(mockDriver4.toConstructorDriver(), 20.0),
//                    Pair(mockDriver2.toConstructorDriver(), 10.0)
//                ), AnimationSpeed.NONE
//            ),
//            RaceModel.ConstructorStandings(
//                mockConstructorAlpha, 20.0, listOf(
//                    Pair(mockDriver3.toConstructorDriver(), 15.0),
//                    Pair(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(), 5.0)
//                ), AnimationSpeed.NONE
//            )
//        )
//
//        initSUT(orderBy = CONSTRUCTOR_STANDINGS)
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(CONSTRUCTOR_STANDINGS, expected, expectedSeasonRound))
//        }
//    }
//
//    @Test
//    fun `when view type is race (error) and round date is in the future, show race in future unavailable message`() = coroutineTest {
//
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow {
//            emit(MOCK_RACE_1.copy(
//                date = LocalDate.now().plusDays(5L),
//                race = emptyMap()
//            ))
//        }
//        val expected = listOf<RaceModel>(
//            MOCK_RACE_1.getRaceOvevriew(date = LocalDate.now().plusDays(5L)),
//            RaceModel.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.RACE_IN_FUTURE)),
//        )
//
//        initSUT()
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(RACE, expected, expectedSeasonRound))
//        }
//    }
//
//    @Test
//    fun `when view type is race (error) and round date is in the past, show race data coming soon unavailable message`() = coroutineTest {
//
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow {
//            emit(MOCK_RACE_1.copy(
//                date = LocalDate.now().minusDays(5L),
//                race = emptyMap()
//            ))
//        }
//        val expected = listOf<RaceModel>(
//            MOCK_RACE_1.getRaceOvevriew(date = LocalDate.now().minusDays(5L)),
//            RaceModel.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.COMING_SOON_RACE)),
//        )
//
//        initSUT()
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(RACE, expected, expectedSeasonRound))
//        }
//    }
//
//    @Test
//    fun `when view type is race (happy) and roundData race is not empty, show podium + race results in list`() = coroutineTest {
//
//        every { mockRaceController.showQualifyingDelta } returns false
//        every { mockRaceController.showGridPenaltiesInQualifying } returns false
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(MOCK_RACE_1) }
//
//        val expected = listOf(
//            MOCK_RACE_1.getRaceOvevriew(),
//            RaceModel.Podium(
//                convertDriverToSingle(
//                    race = MOCK_RACE_1, roundDriver = mockDriver4.toConstructorDriver(),
//                    expectedQualified = 4,
//                    expectedGrid = 3,
//                    expectedFinish = 1
//                ),
//                convertDriverToSingle(
//                    race = MOCK_RACE_1, roundDriver = mockDriver3.toConstructorDriver(),
//                    expectedQualified = 3,
//                    expectedGrid = 4,
//                    expectedFinish = 2
//                ),
//                convertDriverToSingle(
//                    race = MOCK_RACE_1, roundDriver = mockDriver2.toConstructorDriver(),
//                    expectedQualified = 2,
//                    expectedGrid = 2,
//                    expectedFinish = 3
//                )
//            ),
//            RaceModel.RaceHeader(expectedSeasonRound.first, expectedSeasonRound.second),
//            convertDriverToSingle(race = MOCK_RACE_1, roundDriver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(),
//                expectedQualified = 1,
//                expectedGrid = 1,
//                expectedFinish = 4
//            ),
//        )
//
//        initSUT()
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(RACE, expected, expectedSeasonRound))
//        }
//    }
//
//    @Test
//    fun `when view type is qualifying (happy) Q3, items are ordered properly`() = coroutineTest {
//
//        every { mockRaceController.showQualifyingDelta } returns false
//        every { mockRaceController.showGridPenaltiesInQualifying } returns false
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(MOCK_RACE_1) }
//
//        val showQualifying = DisplayPrefs(true, true, true, false, false, true)
//        val expected = mutableListOf<RaceModel>(
//            MOCK_RACE_1.getRaceOvevriew(),
//            RaceModel.QualifyingHeader(showQualifying)
//        )
//        expected.addAll(expectedQ3Order)
//
//        initSUT(orderBy = QUALIFYING_POS)
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(QUALIFYING_POS, expected, expectedSeasonRound))
//        }
//    }
//
//    @Test
//    fun `when view type is qualifying (happy) Q2, items are ordered properly`() = coroutineTest {
//
//        every { mockRaceController.showQualifyingDelta } returns false
//        every { mockRaceController.showGridPenaltiesInQualifying } returns false
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(MOCK_RACE_1) }
//
//        val showQualifying = DisplayPrefs(true, true, true, false, false, true)
//        val expected = mutableListOf<RaceModel>(
//            MOCK_RACE_1.getRaceOvevriew(),
//            RaceModel.QualifyingHeader(showQualifying)
//        )
//        expected.addAll(expectedQ2Order)
//
//        initSUT(orderBy = QUALIFYING_POS_2)
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(QUALIFYING_POS_2, expected, expectedSeasonRound))
//        }
//    }
//
//    @Test
//    fun `when view type is qualifying (happy) Q1, items are ordered properly`() = coroutineTest {
//
//        every { mockRaceController.showQualifyingDelta } returns false
//        every { mockRaceController.showGridPenaltiesInQualifying } returns false
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(MOCK_RACE_1) }
//
//        val showQualifying = DisplayPrefs(true, true, true, false, false, true)
//        val expected = mutableListOf<RaceModel>(
//            MOCK_RACE_1.getRaceOvevriew(),
//            RaceModel.QualifyingHeader(showQualifying)
//        )
//        expected.addAll(expectedQ1Order)
//
//        initSUT(orderBy = QUALIFYING_POS_1)
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(QUALIFYING_POS_1, expected, expectedSeasonRound))
//        }
//    }
//
//    @Test
//    fun `when order by is changed list content updates`() = coroutineTest {
//
//        every { mockRaceController.showQualifyingDelta } returns false
//        every { mockRaceController.showGridPenaltiesInQualifying } returns false
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(MOCK_RACE_1) }
//
//        val showQualifying = DisplayPrefs(true, true, true, false, false, true)
//        val expectedQ3 = mutableListOf<RaceModel>(
//            MOCK_RACE_1.getRaceOvevriew(),
//            RaceModel.QualifyingHeader(showQualifying)
//        )
//        expectedQ3.addAll(expectedQ3Order)
//
//        val expectedQ2 = mutableListOf<RaceModel>(
//            MOCK_RACE_1.getRaceOvevriew(),
//            RaceModel.QualifyingHeader(showQualifying)
//        )
//        expectedQ2.addAll(expectedQ2Order)
//
//        val expectedQ1 = mutableListOf<RaceModel>(
//            MOCK_RACE_1.getRaceOvevriew(),
//            RaceModel.QualifyingHeader(showQualifying)
//        )
//        expectedQ1.addAll(expectedQ1Order)
//
//        initSUT(orderBy = QUALIFYING_POS)
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(QUALIFYING_POS, expectedQ3, expectedSeasonRound))
//        }
//
//        sut.inputs.orderBy(QUALIFYING_POS_1)
//        advanceUntilIdle()
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(QUALIFYING_POS_1, expectedQ1, expectedSeasonRound))
//        }
//
//        sut.inputs.orderBy(QUALIFYING_POS_2)
//        advanceUntilIdle()
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(QUALIFYING_POS_2, expectedQ2, expectedSeasonRound))
//        }
//    }
//
//    @Test
//    fun `when show qualifying delta is enabled, qualifying delta is supplied`() = coroutineTest {
//
//        every { mockRaceController.showQualifyingDelta } returns true
//        every { mockRaceController.showGridPenaltiesInQualifying } returns false
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(MOCK_RACE_1) }
//
//        val showQualifying = DisplayPrefs(true, true, true, true, false, true)
//        val expected = mutableListOf<RaceModel>(
//            MOCK_RACE_1.getRaceOvevriew(),
//            RaceModel.QualifyingHeader(showQualifying)
//        )
//        expected.addAll(expectedQ3OrderWithQualifyingDeltas)
//
//        initSUT(orderBy = QUALIFYING_POS)
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(QUALIFYING_POS, expected, expectedSeasonRound))
//        }
//    }
//
//    @Test
//    fun `when show qualifying delta is disabled, toggling it shows it's enabled`() = coroutineTest {
//
//        every { mockRaceController.showQualifyingDelta } returns false
//        every { mockRaceController.showGridPenaltiesInQualifying } returns false
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(MOCK_RACE_1) }
//
//        val showQualifying = DisplayPrefs(true, true, true, true, false, true)
//        val expected = mutableListOf<RaceModel>(
//            MOCK_RACE_1.getRaceOvevriew(),
//            RaceModel.QualifyingHeader(showQualifying)
//        )
//        expected.addAll(expectedQ3OrderWithQualifyingDeltas)
//
//        initSUT(orderBy = QUALIFYING_POS)
//
//        sut.inputs.toggleQualifyingDelta(true)
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(QUALIFYING_POS, expected, expectedSeasonRound))
//        }
//    }
//
//    @Test
//    fun `when only q1 data is supplied, ordering for multiple qualifying types always does the same order`() = coroutineTest {
//
//        every { mockRaceController.showQualifyingDelta } returns false
//        every { mockRaceController.showGridPenaltiesInQualifying } returns false
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound3) }
//
//        val showQualifying = DisplayPrefs(true, false, false, false, false, true)
//        val expected = mutableListOf<RaceModel>(
//            mockRound3.getRaceOvevriew(),
//            RaceModel.QualifyingHeader(showQualifying)
//        )
//        expected.addAll(expectedQ3Order(race = mockRound3, displayPrefs = showQualifying))
//
//        initSUT(orderBy = QUALIFYING_POS)
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(QUALIFYING_POS, expected, SeasonRound(2019, 3)))
//        }
//
//        // QUALIFYING_POS_1 not available in the UI
//
//        initSUT(orderBy = QUALIFYING_POS_2)
//        advanceUntilIdle()
//
//        sut.outputs.raceItems.test {
//            assertValue(Triple(QUALIFYING_POS_2, expected, SeasonRound(2019, 3)))
//        }
//    }
//
//    @Test
//    fun `when sprint qualifying doesnt exist the tab is hidden`() {
//
//        every { mockRaceController.showQualifyingDelta } returns false
//        every { mockRaceController.showGridPenaltiesInQualifying } returns false
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound3) }
//
//        initSUT(orderBy = QUALIFYING_POS)
//
//        sut.outputs.showSprintQualifying.test {
//            assertValue(false)
//        }
//    }
//
//    @Test
//    fun `when sprint qualifying data exists the tab is shown`() = coroutineTest {
//
//        every { mockRaceController.showQualifyingDelta } returns false
//        every { mockRaceController.showGridPenaltiesInQualifying } returns false
//        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound3.copy(
//            qSprint = mapOf(
//                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id to RaceSprintQualifyingResult(
//                    driver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(),
//                    time = LapTime(),
//                    points = 1.0,
//                    grid = 1,
//                    qualified = 1,
//                    finish = 1,
//                    status = "Finished"
//                )
//            )
//        )) }
//
//        initSUT(orderBy = QUALIFYING_SPRINT)
//        advanceUntilIdle()
//
//        sut.outputs.raceItems.test {
//            assertEmittedCount(1)
//        }
//        sut.outputs.showSprintQualifying.test {
//            assertValue(true)
//        }
//    }
//
//    @Test
//    fun `clicking go to driver with driver id and name fires go to driver event`() {
//
//        val expectedDriverId = "driver-id"
//        val expectedDriverName = "driver-name"
//        initSUT()
//
//        sut.inputs.goToDriver(expectedDriverId, expectedDriverName)
//
//        sut.outputs.goToDriverOverview.test {
//            assertDataEventValue(Pair(expectedDriverId, expectedDriverName))
//        }
//    }
//
//    @Test
//    fun `clicking go to constructor with constructor id and name fires go to constructor event`() {
//
//        val expectedConstructorId = "driver-id"
//        val expectedConstructorName = "driver-name"
//        initSUT()
//
//        sut.inputs.goToConstructor(expectedConstructorId, expectedConstructorName)
//
//        sut.outputs.goToConstructorOverview.test {
//            assertDataEventValue(Pair(expectedConstructorId, expectedConstructorName))
//        }
//    }
//
//    //region Round 1 expected qualifying orders
//
//    private val expectedQ3Order: List<RaceModel> = listOf(
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(),
//            expectedQualified = 1,
//            expectedGrid = 1,
//            expectedFinish = 4
//        ),
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = mockDriver2.toConstructorDriver(),
//            expectedQualified = 2,
//            expectedGrid = 2,
//            expectedFinish = 3
//        ),
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = mockDriver3.toConstructorDriver(),
//            expectedQualified = 3,
//            expectedGrid = 4,
//            expectedFinish = 2
//        ),
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = mockDriver4.toConstructorDriver(),
//            expectedQualified = 4,
//            expectedGrid = 3,
//            expectedFinish = 1
//        )
//    )
//
//    private fun expectedQ3Order(race: Race = MOCK_RACE_1, displayPrefs: DisplayPrefs = DisplayPrefs(
//        q1 = true,
//        q2 = true,
//        q3 = true,
//        penalties = false,
//        deltas = false,
//        fadeDNF = true
//    )
//    ): List<RaceModel> = listOf(
//        convertDriverToSingle(race = race, roundDriver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(),
//            expectedQualified = 1,
//            expectedGrid = 1,
//            expectedFinish = 4,
//            displayPrefs = displayPrefs
//        ),
//        convertDriverToSingle(race = race, roundDriver = mockDriver2.toConstructorDriver(),
//            expectedQualified = 2,
//            expectedGrid = 2,
//            expectedFinish = 3,
//            displayPrefs = displayPrefs
//        ),
//        convertDriverToSingle(race = race, roundDriver = mockDriver3.toConstructorDriver(),
//            expectedQualified = 3,
//            expectedGrid = 4,
//            expectedFinish = 2,
//            displayPrefs = displayPrefs
//        ),
//        convertDriverToSingle(race = race, roundDriver = mockDriver4.toConstructorDriver(),
//            expectedQualified = 4,
//            expectedGrid = 3,
//            expectedFinish = 1,
//            displayPrefs = displayPrefs
//        )
//    )
//
//    private val expectedQ2Order: List<RaceModel> = listOf(
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = mockDriver2.toConstructorDriver(),
//            expectedQualified = 2,
//            expectedGrid = 2,
//            expectedFinish = 3
//        ),
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(),
//            expectedQualified = 1,
//            expectedGrid = 1,
//            expectedFinish = 4
//        ),
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = mockDriver3.toConstructorDriver(),
//            expectedQualified = 3,
//            expectedGrid = 4,
//            expectedFinish = 2
//        ),
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = mockDriver4.toConstructorDriver(),
//            expectedQualified = 4,
//            expectedGrid = 3,
//            expectedFinish = 1
//        )
//    )
//    private val expectedQ1Order: List<RaceModel> = listOf(
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(),
//            expectedQualified = 1,
//            expectedGrid = 1,
//            expectedFinish = 4
//        ),
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = mockDriver3.toConstructorDriver(),
//            expectedQualified = 3,
//            expectedGrid = 4,
//            expectedFinish = 2
//        ),
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = mockDriver2.toConstructorDriver(),
//            expectedQualified = 2,
//            expectedGrid = 2,
//            expectedFinish = 3
//        ),
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = mockDriver4.toConstructorDriver(),
//            expectedQualified = 4,
//            expectedGrid = 3,
//            expectedFinish = 1
//        )
//    )
//
//    //endregion
//
//
//    //region Round 1 Qualifying deltas check
//
//    private val expectedQ3OrderWithQualifyingDeltas: List<RaceModel> = listOf(
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(),
//            expectedQualified = 1,
//            expectedGrid = 1,
//            expectedFinish = 4,
//            expectedQ1Delta = null,
//            expectedQ2Delta = "+1.000",
//            expectedQ3Delta = null,
//            displayPrefs = DisplayPrefs(true, true, true, true, false, true)
//        ),
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = mockDriver2.toConstructorDriver(),
//            expectedQualified = 2,
//            expectedGrid = 2,
//            expectedFinish = 3,
//            expectedQ1Delta = "+2.000",
//            expectedQ2Delta = null,
//            expectedQ3Delta = "+1.000",
//            displayPrefs = DisplayPrefs(true, true, true, true, false, true)
//        ),
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = mockDriver3.toConstructorDriver(),
//            expectedQualified = 3,
//            expectedGrid = 4,
//            expectedFinish = 2,
//            expectedQ1Delta = "+1.000",
//            expectedQ2Delta = "+2.000",
//            expectedQ3Delta = null,
//            displayPrefs = DisplayPrefs(true, true, true, true, false, true)
//        ),
//        convertDriverToSingle(race = MOCK_RACE_1, roundDriver = mockDriver4.toConstructorDriver(),
//            expectedQualified = 4,
//            expectedGrid = 3,
//            expectedFinish = 1,
//            expectedQ1Delta = "+3.000",
//            expectedQ2Delta = null,
//            expectedQ3Delta = null,
//            displayPrefs = DisplayPrefs(true, true, true, true, false, true)
//        )
//    )
//
//    //endregion
//
//    private fun Race.getRaceOvevriew(
//        date: LocalDate = this.date
//    ): RaceModel.Overview {
//        return RaceModel.Overview(
//            raceName = this.name,
//            country = this.circuit.country,
//            countryISO = this.circuit.countryISO,
//            circuitId = this.circuit.id,
//            circuitName = this.circuit.name,
//            round = this.round,
//            season = this.season,
//            raceDate = date,
//            wikipedia = this.wikipediaUrl
//        )
//    }
//
//    private fun convertDriverToSingle(race: Race, roundDriver: ConstructorDriver,
//                                      expectedGrid: Int,
//                                      expectedFinish: Int,
//                                      expectedQualified: Int,
//                                      expectedQ1Delta: String? = null,
//                                      expectedQ2Delta: String? = null,
//                                      expectedQ3Delta: String? = null,
//                                      displayPrefs: DisplayPrefs = DisplayPrefs(
//                                          q1 = true,
//                                          q2 = true,
//                                          q3 = true,
//                                          penalties = false,
//                                          deltas = false,
//                                          fadeDNF = true
//                                      )
//    ): RaceModel.Single {
//        val overview = race.driverOverview(roundDriver.id)
//        return RaceModel.Single(
//            season = race.season,
//            round = race.round,
//            driver = roundDriver,
//            q1 = overview.q1,
//            q2 = overview.q2,
//            q3 = overview.q3,
//            qSprint = overview.qSprint,
//            race = overview.race?.let {
//                SingleRace(
//                    points = it.points,
//                    result = it.time ?: LapTime(),
//                    pos = expectedFinish,
//                    gridPos = expectedGrid,
//                    status = it.status,
//                    fastestLap = it.fastestLap?.rank == 1
//                )
//            },
//            qualified = expectedQualified,
//            q1Delta = expectedQ1Delta,
//            q2Delta = expectedQ2Delta,
//            q3Delta = expectedQ3Delta,
//            displayPrefs = displayPrefs
//        )
//    }
//}