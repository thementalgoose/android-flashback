package tmg.flashback.statistics.ui.race

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.statistics.controllers.RaceController
import tmg.flashback.core.controllers.AppHintsController
import tmg.flashback.core.controllers.AppearanceController
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.data.db.stats.SeasonOverviewRepository
import tmg.flashback.data.models.stats.LapTime
import tmg.flashback.data.models.stats.Round
import tmg.flashback.data.models.stats.RoundDriver
import tmg.flashback.statistics.mockRound1
import tmg.flashback.statistics.mockRound3
import tmg.flashback.statistics.testutils.*
import tmg.flashback.statistics.testutils.BaseTest
import tmg.flashback.statistics.testutils.assertDataEventValue
import tmg.flashback.statistics.testutils.assertEventFired
import tmg.flashback.statistics.testutils.test
import tmg.flashback.statistics.ui.overviews.*
import tmg.flashback.statistics.ui.race.RaceAdapterType.*
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.flashback.statistics.ui.util.SeasonRound

internal class RaceViewModelTest: BaseTest() {

    lateinit var sut: RaceViewModel

    private val mockSeasonOverviewRepository: SeasonOverviewRepository = mockk(relaxed = true)
    private val mockAppearanceController: AppearanceController = mockk(relaxed = true)
    private val mockRaceController: RaceController = mockk(relaxed = true)
    private val mockAppHintsController: AppHintsController = mockk(relaxed = true)
    private val mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)

    private val expectedSeasonRound: SeasonRound = SeasonRound(2019, 1)

    @BeforeEach
    internal fun setUp() {

        every { mockConnectivityManager.isConnected } returns true
        every { mockAppearanceController.animationSpeed } returns AnimationSpeed.NONE
        every { mockRaceController.fadeDNF } returns true
    }

    private fun initSUT(roundDate: LocalDate? = null, orderBy: RaceAdapterType = RACE) {
        sut = RaceViewModel(mockSeasonOverviewRepository, mockAppHintsController, mockRaceController, mockAppearanceController, mockConnectivityManager)
        val (season, round) = expectedSeasonRound
        sut.inputs.initialise(season, round, roundDate)
        sut.inputs.orderBy(orderBy)
    }

    //region Network

    @Test
    fun `init no network error shown when network isnt available`() = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(null) }
        every { mockConnectivityManager.isConnected } returns false

        initSUT()

        sut.outputs.raceItems.test {
            assertValue(Triple(
                RACE,
                listOf(RaceModel.ErrorItem(SyncDataItem.NoNetwork)),
                expectedSeasonRound
            ))
        }
    }

    //endregion

    @Test
    fun `when round data is null and date supplied is in the future, show race in future unavailable message`() = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(null) }

        initSUT(LocalDate.now().plusDays(1L))

        sut.outputs.raceItems.test {
            assertValue(Triple(
                RACE,
                listOf(RaceModel.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_RACE))),
                expectedSeasonRound
            ))
        }
    }

    @Test
    fun `when round data is null and round date is in the past, show coming soon race data unavailable message`() = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(null) }

        initSUT(LocalDate.now().minusDays(1L))

        sut.outputs.raceItems.test {
            assertValue(Triple(RACE, listOf(
                RaceModel.ErrorItem(
                    SyncDataItem.Unavailable(
                        DataUnavailable.COMING_SOON_RACE
                    )
                )
            ), expectedSeasonRound))
        }
    }

    @Test
    fun `when round data is null and date supplied is null, show missing race data unavailable message`() = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(null) }

        initSUT(null)

        sut.outputs.raceItems.test {
            assertValue(Triple(RACE, listOf(
                RaceModel.ErrorItem(
                    SyncDataItem.Unavailable(
                        DataUnavailable.MISSING_RACE
                    )
                )
            ), expectedSeasonRound))
        }
    }

    @Test
    fun `when round data is null and the round happened within the past 10 days, show the race is coming soon message`() = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(null) }
        val showComingSoonMessageForNextDays = 5

        initSUT(LocalDate.now().minusDays(showComingSoonMessageForNextDays - 1L))

        sut.outputs.raceItems.test {
            assertValue(Triple(RACE, listOf(
                RaceModel.ErrorItem(
                    SyncDataItem.Unavailable(
                        DataUnavailable.COMING_SOON_RACE
                    )
                )
            ), expectedSeasonRound))
        }
    }

    @Test
    fun `when round data is null and the round is happening or happened today, show the race is coming soon message`() = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(null) }

        initSUT(LocalDate.now())

        sut.outputs.raceItems.test {
            assertValue(Triple(RACE, listOf(
                RaceModel.ErrorItem(
                    SyncDataItem.Unavailable(
                        DataUnavailable.COMING_SOON_RACE
                    )
                )
            ), expectedSeasonRound))
        }
    }

    @Test
    fun `when view type is (happy) constructor, standings show constructor standings items with list of drivers`() = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound1) }

        val expected = listOf<RaceModel>(
            RaceModel.ConstructorStandings(
                mockConstructorBeta, 30, listOf(
                    Pair(mockDriver4.toDriver(), 20),
                    Pair(mockDriver2.toDriver(), 10)
                ), AnimationSpeed.NONE
            ),
            RaceModel.ConstructorStandings(
                mockConstructorAlpha, 20, listOf(
                    Pair(mockDriver3.toDriver(), 15),
                    Pair(mockDriver1.toDriver(), 5)
                ), AnimationSpeed.NONE
            )
        )

        initSUT(orderBy = CONSTRUCTOR_STANDINGS)

        sut.outputs.raceItems.test {
            assertValue(Triple(CONSTRUCTOR_STANDINGS, expected, expectedSeasonRound))
        }
    }

    @Test
    fun `when view type is race (error) and round date is in the future, show race in future unavailable message`() = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow {
            emit(mockRound1.copy(
                date = LocalDate.now().plusDays(5L),
                race = emptyMap()
            ))
        }
        val expected = listOf<RaceModel>(
            RaceModel.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_RACE)),
            RaceModel.ErrorItem(SyncDataItem.ProvidedBy())
        )

        initSUT()

        sut.outputs.raceItems.test {
            assertValue(Triple(RACE, expected, expectedSeasonRound))
        }
    }

    @Test
    fun `when view type is race (error) and round date is in the past, show race data coming soon unavailable message`() = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow {
            emit(mockRound1.copy(
                date = LocalDate.now().minusDays(5L),
                race = emptyMap()
            ))
        }
        val expected = listOf<RaceModel>(
            RaceModel.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.COMING_SOON_RACE)),
            RaceModel.ErrorItem(SyncDataItem.ProvidedBy())
        )

        initSUT()

        sut.outputs.raceItems.test {
            assertValue(Triple(RACE, expected, expectedSeasonRound))
        }
    }

    @Test
    fun `when view type is race (happy) and roundData race is not empty, show podium + race results in list`() = coroutineTest {

        every { mockRaceController.showQualifyingDelta } returns false
        every { mockRaceController.showGridPenaltiesInQualifying } returns false
        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound1) }

        val expected = listOf(
            RaceModel.Podium(
                convertDriverToSingle(
                    round = mockRound1, roundDriver = mockDriver4,
                    expectedQualified = 4,
                    expectedGrid = 3,
                    expectedFinish = 1
                ),
                convertDriverToSingle(
                    round = mockRound1, roundDriver = mockDriver3,
                    expectedQualified = 3,
                    expectedGrid = 4,
                    expectedFinish = 2
                ),
                convertDriverToSingle(
                    round = mockRound1, roundDriver = mockDriver2,
                    expectedQualified = 2,
                    expectedGrid = 2,
                    expectedFinish = 3
                )
            ),
            RaceModel.RaceHeader(expectedSeasonRound.first, expectedSeasonRound.second),
            convertDriverToSingle(round = mockRound1, roundDriver = mockDriver1,
                expectedQualified = 1,
                expectedGrid = 1,
                expectedFinish = 4
            ),
            RaceModel.ErrorItem(SyncDataItem.ProvidedBy())
        )

        initSUT()

        sut.outputs.raceItems.test {
            assertValue(Triple(RACE, expected, expectedSeasonRound))
        }
    }

    @Test
    fun `when view type is qualifying (happy) Q3, items are ordered properly`() = coroutineTest {

        every { mockRaceController.showQualifyingDelta } returns false
        every { mockRaceController.showGridPenaltiesInQualifying } returns false
        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound1) }

        val showQualifying = DisplayPrefs(true, true, true, false, false, true)
        val expected = mutableListOf<RaceModel>(RaceModel.QualifyingHeader(showQualifying))
        expected.addAll(expectedQ3Order)
        expected.add(RaceModel.ErrorItem(SyncDataItem.ProvidedBy()))

        initSUT(orderBy = QUALIFYING_POS)

        sut.outputs.raceItems.test {
            assertValue(Triple(QUALIFYING_POS, expected, expectedSeasonRound))
        }
    }

    @Test
    fun `when view type is qualifying (happy) Q2, items are ordered properly`() = coroutineTest {

        every { mockRaceController.showQualifyingDelta } returns false
        every { mockRaceController.showGridPenaltiesInQualifying } returns false
        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound1) }

        val showQualifying = DisplayPrefs(true, true, true, false, false, true)
        val expected = mutableListOf<RaceModel>(RaceModel.QualifyingHeader(showQualifying))
        expected.addAll(expectedQ2Order)
        expected.add(RaceModel.ErrorItem(SyncDataItem.ProvidedBy()))

        initSUT(orderBy = QUALIFYING_POS_2)

        sut.outputs.raceItems.test {
            assertValue(Triple(QUALIFYING_POS_2, expected, expectedSeasonRound))
        }
    }

    @Test
    fun `when view type is qualifying (happy) Q1, items are ordered properly`() = coroutineTest {

        every { mockRaceController.showQualifyingDelta } returns false
        every { mockRaceController.showGridPenaltiesInQualifying } returns false
        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound1) }

        val showQualifying = DisplayPrefs(true, true, true, false, false, true)
        val expected = mutableListOf<RaceModel>(RaceModel.QualifyingHeader(showQualifying))
        expected.addAll(expectedQ1Order)
        expected.add(RaceModel.ErrorItem(SyncDataItem.ProvidedBy()))

        initSUT(orderBy = QUALIFYING_POS_1)

        sut.outputs.raceItems.test {
            assertValue(Triple(QUALIFYING_POS_1, expected, expectedSeasonRound))
        }
    }

    @Test
    fun `when order by is changed list content updates`() = coroutineTest {

        every { mockRaceController.showQualifyingDelta } returns false
        every { mockRaceController.showGridPenaltiesInQualifying } returns false
        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound1) }

        val showQualifying = DisplayPrefs(true, true, true, false, false, true)
        val expectedQ3 = mutableListOf<RaceModel>(RaceModel.QualifyingHeader(showQualifying))
        expectedQ3.addAll(expectedQ3Order)
        expectedQ3.add(RaceModel.ErrorItem(SyncDataItem.ProvidedBy()))

        val expectedQ2 = mutableListOf<RaceModel>(RaceModel.QualifyingHeader(showQualifying))
        expectedQ2.addAll(expectedQ2Order)
        expectedQ2.add(RaceModel.ErrorItem(SyncDataItem.ProvidedBy()))

        val expectedQ1 = mutableListOf<RaceModel>(RaceModel.QualifyingHeader(showQualifying))
        expectedQ1.addAll(expectedQ1Order)
        expectedQ1.add(RaceModel.ErrorItem(SyncDataItem.ProvidedBy()))

        initSUT(orderBy = QUALIFYING_POS)

        sut.outputs.raceItems.test {
            assertValue(Triple(QUALIFYING_POS, expectedQ3, expectedSeasonRound))
        }

        sut.inputs.orderBy(QUALIFYING_POS_1)
        advanceUntilIdle()

        sut.outputs.raceItems.test {
            assertValue(Triple(QUALIFYING_POS_1, expectedQ1, expectedSeasonRound))
        }

        sut.inputs.orderBy(QUALIFYING_POS_2)
        advanceUntilIdle()

        sut.outputs.raceItems.test {
            assertValue(Triple(QUALIFYING_POS_2, expectedQ2, expectedSeasonRound))
        }
    }

    @Test
    fun `when show qualifying delta is enabled, qualifying delta is supplied`() = coroutineTest {

        every { mockRaceController.showQualifyingDelta } returns true
        every { mockRaceController.showGridPenaltiesInQualifying } returns false
        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound1) }

        val showQualifying = DisplayPrefs(true, true, true, true, false, true)
        val expected = mutableListOf<RaceModel>(RaceModel.QualifyingHeader(showQualifying))
        expected.addAll(expectedQ3OrderWithQualifyingDeltas)
        expected.add(RaceModel.ErrorItem(SyncDataItem.ProvidedBy()))

        initSUT(orderBy = QUALIFYING_POS)

        sut.outputs.raceItems.test {
            assertValue(Triple(QUALIFYING_POS, expected, expectedSeasonRound))
        }
    }

    @Test
    fun `when show qualifying delta is disabled, toggling it shows it's enabled`() = coroutineTest {

        every { mockRaceController.showQualifyingDelta } returns false
        every { mockRaceController.showGridPenaltiesInQualifying } returns false
        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound1) }

        val showQualifying = DisplayPrefs(true, true, true, true, false, true)
        val expected = mutableListOf<RaceModel>(RaceModel.QualifyingHeader(showQualifying))
        expected.addAll(expectedQ3OrderWithQualifyingDeltas)
        expected.add(RaceModel.ErrorItem(SyncDataItem.ProvidedBy()))

        initSUT(orderBy = QUALIFYING_POS)

        sut.inputs.toggleQualifyingDelta(true)

        sut.outputs.raceItems.test {
            assertValue(Triple(QUALIFYING_POS, expected, expectedSeasonRound))
        }
    }

    @Test
    fun `when only q1 data is supplied, ordering for multiple qualifying types always does the same order`() = coroutineTest {

        every { mockRaceController.showQualifyingDelta } returns false
        every { mockRaceController.showGridPenaltiesInQualifying } returns false
        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound3) }

        val showQualifying = DisplayPrefs(true, false, false, false, false, true)
        val expected = mutableListOf<RaceModel>(RaceModel.QualifyingHeader(showQualifying))
        expected.addAll(expectedQ3Order(round = mockRound3, displayPrefs = showQualifying))
        expected.add(RaceModel.ErrorItem(SyncDataItem.ProvidedBy()))

        initSUT(orderBy = QUALIFYING_POS)

        sut.outputs.raceItems.test {
            assertValue(Triple(QUALIFYING_POS, expected, SeasonRound(2019, 3)))
        }

        // QUALIFYING_POS_1 not available in the UI

        initSUT(orderBy = QUALIFYING_POS_2)
        advanceUntilIdle()

        sut.outputs.raceItems.test {
            assertValue(Triple(QUALIFYING_POS_2, expected, SeasonRound(2019, 3)))
        }
    }

    @Test
    fun `clicking go to driver with driver id and name fires go to driver event`() {

        val expectedDriverId = "driver-id"
        val expectedDriverName = "driver-name"
        initSUT()

        sut.inputs.goToDriver(expectedDriverId, expectedDriverName)

        sut.outputs.goToDriverOverview.test {
            assertDataEventValue(Pair(expectedDriverId, expectedDriverName))
        }
    }

    @Test
    fun `clicking go to constructor with constructor id and name fires go to constructor event`() {

        val expectedConstructorId = "driver-id"
        val expectedConstructorName = "driver-name"
        initSUT()

        sut.inputs.goToConstructor(expectedConstructorId, expectedConstructorName)

        sut.outputs.goToConstructorOverview.test {
            assertDataEventValue(Pair(expectedConstructorId, expectedConstructorName))
        }
    }

    @Test
    fun `initialise emits season round data`() = coroutineTest {

        initSUT()

        sut.inputs.initialise(2020, 1, LocalDate.now())
        advanceUntilIdle()

        sut.outputs.seasonRoundData.test {
            assertValue(SeasonRound(2020, 1))
        }
    }

    @Test
    fun `initialisation sets wikipedia button to not be shown`() = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound1.copy(wikipediaUrl = null)) }

        initSUT()

        advanceUntilIdle()

        sut.outputs.raceItems.test {
            assertItemCount(1)
        }
        sut.outputs.showLinks.test {
            assertValue(false)
        }
    }

    @Test
    fun `initialisation sets wikipedia button to true if round data contains wikipedia link`() = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound1) }

        initSUT()

        advanceUntilIdle()

        sut.outputs.raceItems.test {
            assertItemCount(1)
        }
        sut.outputs.showLinks.test {
            assertValue(true)
        }
    }

    @Test
    fun `clicking wikipedia button fires goToWikipedia event`() = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound1) }

        initSUT()

        sut.inputs.clickWikipedia()

        sut.outputs.goToWikipedia.test {
            assertEventFired()
        }
    }

    //region App Hints - Long Press Race Qualifying

    @Test
    fun `notify app show qualifying long click hint when never been done before`() = coroutineTest {

        every { mockAppHintsController.showQualifyingLongPress } returns true
        every { mockRaceController.showQualifyingDelta } returns false
        every { mockRaceController.showGridPenaltiesInQualifying } returns false
        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound3) }

        val showQualifying = DisplayPrefs(true, false, false, false, false, true)
        val expected = mutableListOf<RaceModel>(RaceModel.QualifyingHeader(showQualifying))
        expected.addAll(expectedQ3Order(round = mockRound3, displayPrefs = showQualifying))
        expected.add(RaceModel.ErrorItem(SyncDataItem.ProvidedBy()))

        initSUT(orderBy = QUALIFYING_POS)

        sut.outputs.raceItems.test {
            assertValue(Triple(QUALIFYING_POS, expected, SeasonRound(2019, 3)))
        }
        advanceUntilIdle()

        sut.outputs.showAppHintLongPress.test {
            assertEventFired()
        }
        verify { mockAppHintsController.showQualifyingLongPress = true }
    }

    @Test
    fun `notify app show qualifying not shown if it's been shown before`() = coroutineTest {

        every { mockAppHintsController.showQualifyingLongPress } returns false
        every { mockRaceController.showQualifyingDelta } returns false
        every { mockRaceController.showGridPenaltiesInQualifying } returns false
        every { mockSeasonOverviewRepository.getSeasonRound(any(), any()) } returns flow { emit(mockRound3) }

        val showQualifying = DisplayPrefs(true, false, false, false, false, true)
        val expected = mutableListOf<RaceModel>(RaceModel.QualifyingHeader(showQualifying))
        expected.addAll(expectedQ3Order(round = mockRound3, displayPrefs = showQualifying))
        expected.add(RaceModel.ErrorItem(SyncDataItem.ProvidedBy()))

        initSUT(orderBy = QUALIFYING_POS)

        sut.outputs.raceItems.test {
            assertValue(Triple(QUALIFYING_POS, expected, SeasonRound(2019, 3)))
        }
        advanceUntilIdle()

        sut.outputs.showAppHintLongPress.test {
            assertEventNotFired()
        }
    }

    //endregion

    //region Round 1 expected qualifying orders

    private val expectedQ3Order: List<RaceModel> = listOf(
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver1,
            expectedQualified = 1,
            expectedGrid = 1,
            expectedFinish = 4
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver2,
            expectedQualified = 2,
            expectedGrid = 2,
            expectedFinish = 3
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver3,
            expectedQualified = 3,
            expectedGrid = 4,
            expectedFinish = 2
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver4,
            expectedQualified = 4,
            expectedGrid = 3,
            expectedFinish = 1
        )
    )

    private fun expectedQ3Order(round: Round = mockRound1, displayPrefs: DisplayPrefs = DisplayPrefs(
        q1 = true,
        q2 = true,
        q3 = true,
        penalties = false,
        deltas = false,
        fadeDNF = true
    )
    ): List<RaceModel> = listOf(
        convertDriverToSingle(round = round, roundDriver = mockDriver1,
            expectedQualified = 1,
            expectedGrid = 1,
            expectedFinish = 4,
            displayPrefs = displayPrefs
        ),
        convertDriverToSingle(round = round, roundDriver = mockDriver2,
            expectedQualified = 2,
            expectedGrid = 2,
            expectedFinish = 3,
            displayPrefs = displayPrefs
        ),
        convertDriverToSingle(round = round, roundDriver = mockDriver3,
            expectedQualified = 3,
            expectedGrid = 4,
            expectedFinish = 2,
            displayPrefs = displayPrefs
        ),
        convertDriverToSingle(round = round, roundDriver = mockDriver4,
            expectedQualified = 4,
            expectedGrid = 3,
            expectedFinish = 1,
            displayPrefs = displayPrefs
        )
    )

    private val expectedQ2Order: List<RaceModel> = listOf(
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver2,
            expectedQualified = 2,
            expectedGrid = 2,
            expectedFinish = 3
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver1,
            expectedQualified = 1,
            expectedGrid = 1,
            expectedFinish = 4
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver3,
            expectedQualified = 3,
            expectedGrid = 4,
            expectedFinish = 2
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver4,
            expectedQualified = 4,
            expectedGrid = 3,
            expectedFinish = 1
        )
    )
    private val expectedQ1Order: List<RaceModel> = listOf(
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver1,
            expectedQualified = 1,
            expectedGrid = 1,
            expectedFinish = 4
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver3,
            expectedQualified = 3,
            expectedGrid = 4,
            expectedFinish = 2
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver2,
            expectedQualified = 2,
            expectedGrid = 2,
            expectedFinish = 3
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver4,
            expectedQualified = 4,
            expectedGrid = 3,
            expectedFinish = 1
        )
    )

    //endregion


    //region Round 1 Qualifying deltas check

    private val expectedQ3OrderWithQualifyingDeltas: List<RaceModel> = listOf(
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver1,
            expectedQualified = 1,
            expectedGrid = 1,
            expectedFinish = 4,
            expectedQ1Delta = null,
            expectedQ2Delta = "+1.000",
            expectedQ3Delta = null,
            displayPrefs = DisplayPrefs(true, true, true, true, false, true)
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver2,
            expectedQualified = 2,
            expectedGrid = 2,
            expectedFinish = 3,
            expectedQ1Delta = "+2.000",
            expectedQ2Delta = null,
            expectedQ3Delta = "+1.000",
            displayPrefs = DisplayPrefs(true, true, true, true, false, true)
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver3,
            expectedQualified = 3,
            expectedGrid = 4,
            expectedFinish = 2,
            expectedQ1Delta = "+1.000",
            expectedQ2Delta = "+2.000",
            expectedQ3Delta = null,
            displayPrefs = DisplayPrefs(true, true, true, true, false, true)
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver4,
            expectedQualified = 4,
            expectedGrid = 3,
            expectedFinish = 1,
            expectedQ1Delta = "+3.000",
            expectedQ2Delta = null,
            expectedQ3Delta = null,
            displayPrefs = DisplayPrefs(true, true, true, true, false, true)
        )
    )

    //endregion



    private fun convertDriverToSingle(round: Round, roundDriver: RoundDriver,
                                      expectedGrid: Int,
                                      expectedFinish: Int,
                                      expectedQualified: Int,
                                      expectedQ1Delta: String? = null,
                                      expectedQ2Delta: String? = null,
                                      expectedQ3Delta: String? = null,
                                      displayPrefs: DisplayPrefs = DisplayPrefs(
                                          q1 = true,
                                          q2 = true,
                                          q3 = true,
                                          penalties = false,
                                          deltas = false,
                                          fadeDNF = true
                                      )
    ): RaceModel.Single {
        val overview = round.driverOverview(roundDriver.id)
        return RaceModel.Single(
            season = round.season,
            round = round.round,
            driver = roundDriver,
            q1 = overview.q1,
            q2 = overview.q2,
            q3 = overview.q3,
            race = overview.race?.let {
                SingleRace(
                    points = it.points,
                    result = it.time ?: LapTime(),
                    pos = expectedFinish,
                    gridPos = expectedGrid,
                    status = it.status,
                    fastestLap = it.fastestLap?.rank == 1
                )
            },
            qualified = expectedQualified,
            q1Delta = expectedQ1Delta,
            q2Delta = expectedQ2Delta,
            q3Delta = expectedQ3Delta,
            displayPrefs = displayPrefs
        )
    }
}