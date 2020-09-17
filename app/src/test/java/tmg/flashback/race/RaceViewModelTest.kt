package tmg.flashback.race

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
import org.threeten.bp.LocalDate
import tmg.flashback.*
import tmg.flashback.race.RaceAdapterType.*
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.stats.SeasonOverviewDB
import tmg.flashback.repo.models.stats.LapTime
import tmg.flashback.repo.models.stats.Round
import tmg.flashback.repo.models.stats.RoundDriver
import tmg.flashback.repo.models.stats.RoundQualifyingResult
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.viewholders.DataUnavailable
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertLatestValue
import tmg.flashback.testutils.assertValue
import tmg.flashback.utils.SeasonRound

@FlowPreview
@ExperimentalCoroutinesApi
class RaceViewModelTest: BaseTest() {

    lateinit var sut: RaceViewModel

    private val mockSeasonOverviewDB: SeasonOverviewDB = mock()
    private val mockPrefsDB: PrefsDB = mock()
    private val mockConnectivityManager: ConnectivityManager = mock()

    private val expectedSeasonRound: SeasonRound = SeasonRound(2019, 1)

    @BeforeEach
    internal fun setUp() {

        whenever(mockConnectivityManager.isConnected).thenReturn(true)
    }

    private fun initSUT(roundDate: LocalDate? = null, orderBy: RaceAdapterType = RACE) {
        sut = RaceViewModel(mockSeasonOverviewDB, mockPrefsDB, mockConnectivityManager, testScopeProvider)
        val (season, round) = expectedSeasonRound
        sut.inputs.initialise(season, round, roundDate)
        sut.inputs.orderBy(orderBy)
    }

    @Test
    fun `RaceViewModel init no network error shown when network isnt available`() = coroutineTest {

        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(null) })
        whenever(mockConnectivityManager.isConnected).thenReturn(false)

        initSUT()

        assertValue(Triple(RACE, listOf(RaceAdapterModel.NoNetwork), expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when round data is null and date supplied is in the future, show race in future unavailable message`() = coroutineTest {

        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(null) })

        initSUT(LocalDate.now().plusDays(1L))

        assertValue(Triple(RACE, listOf(RaceAdapterModel.Unavailable(DataUnavailable.IN_FUTURE_RACE)), expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when round data is null and round date is in the past, show coming soon race data unavailable message`() = coroutineTest {

        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(null) })

        initSUT(LocalDate.now().minusDays(1L))

        assertValue(Triple(RACE, listOf(RaceAdapterModel.Unavailable(DataUnavailable.COMING_SOON_RACE)), expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when round data is null and date supplied is null, show missing race data unavailable message`() = coroutineTest {

        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(null) })

        initSUT(null)

        assertValue(Triple(RACE, listOf(RaceAdapterModel.Unavailable(DataUnavailable.MISSING_RACE)), expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when round data is null and the round happened within the past 10 days, show the race is coming soon message`() = coroutineTest {

        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(null) })
        val showComingSoonMessageForNextDays = 10

        initSUT(LocalDate.now().minusDays(showComingSoonMessageForNextDays - 1L))

        assertValue(Triple(RACE, listOf(RaceAdapterModel.Unavailable(DataUnavailable.COMING_SOON_RACE)), expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when round data is null and the round is happening or happened today, show the race is coming soon message`() = coroutineTest {

        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(null) })

        initSUT(LocalDate.now())

        assertValue(Triple(RACE, listOf(RaceAdapterModel.Unavailable(DataUnavailable.COMING_SOON_RACE)), expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when view type is (happy) constructor with preference to show drivers enabled, standings show constructor standings items with list of drivers`() = coroutineTest {

        whenever(mockPrefsDB.showDriversBehindConstructor).thenReturn(true)
        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(mockRound1) })
        val expected = listOf<RaceAdapterModel>(
            RaceAdapterModel.ConstructorStandings(
                mockConstructorBeta, 30, listOf(
                    Pair(mockDriver4, 20),
                    Pair(mockDriver2, 10)
                )
            ),
            RaceAdapterModel.ConstructorStandings(
                mockConstructorAlpha, 20, listOf(
                    Pair(mockDriver3, 15),
                    Pair(mockDriver1, 5)
                )
            )
        )

        initSUT(orderBy = CONSTRUCTOR_STANDINGS)

        assertValue(Triple(CONSTRUCTOR_STANDINGS, expected, expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when view type is (happy) constructor with preference to show drivers disabled, standings show constructor standings items with no drivers`() = coroutineTest {

        whenever(mockPrefsDB.showDriversBehindConstructor).thenReturn(false)
        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(mockRound1) })
        val expected = listOf<RaceAdapterModel>(
            RaceAdapterModel.ConstructorStandings(
                mockConstructorBeta, 30, emptyList()
            ),
            RaceAdapterModel.ConstructorStandings(
                mockConstructorAlpha, 20, emptyList()
            )
        )

        initSUT(orderBy = CONSTRUCTOR_STANDINGS)

        assertValue(Triple(CONSTRUCTOR_STANDINGS, expected, expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when view type is race (error) and round date is in the future, show race in future unavailable message`() = coroutineTest {

        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(mockRound1.copy(
            date = LocalDate.now().plusDays(5L),
            race = emptyMap()
        )) })
        val expected = listOf<RaceAdapterModel>(
            RaceAdapterModel.Unavailable(DataUnavailable.IN_FUTURE_RACE)
        )

        initSUT()

        assertValue(Triple(RACE, expected, expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when view type is race (error) and round date is in the past, show race data coming soon unavailable message`() = coroutineTest {

        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(mockRound1.copy(
            date = LocalDate.now().minusDays(5L),
            race = emptyMap()
        )) })
        val expected = listOf<RaceAdapterModel>(
            RaceAdapterModel.Unavailable(DataUnavailable.COMING_SOON_RACE)
        )

        initSUT()

        assertValue(Triple(RACE, expected, expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when view type is race (happy) and roundData race is not empty, show podium + race results in list`() = coroutineTest {

        whenever(mockPrefsDB.showQualifyingDelta).thenReturn(false)
        whenever(mockPrefsDB.showGridPenaltiesInQualifying).thenReturn(false)
        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(mockRound1) })
        val expected = listOf(
            RaceAdapterModel.Podium(
                convertDriverToSingle(round = mockRound1, roundDriver = mockDriver4,
                    expectedQualified = 4,
                    expectedGrid = 3,
                    expectedFinish = 1
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
                )
            ),
            RaceAdapterModel.RaceHeader(expectedSeasonRound.first, expectedSeasonRound.second),
            convertDriverToSingle(round = mockRound1, roundDriver = mockDriver1,
                expectedQualified = 1,
                expectedGrid = 1,
                expectedFinish = 4
            )
        )

        initSUT()

        assertValue(Triple(RACE, expected, expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when view type is qualifying (happy) Q3, items are ordered properly`() = coroutineTest {

        val showQualifying = ShowQualifying(true, true, true, false, false)

        whenever(mockPrefsDB.showQualifyingDelta).thenReturn(false)
        whenever(mockPrefsDB.showGridPenaltiesInQualifying).thenReturn(false)
        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(mockRound1) })
        val expected = mutableListOf<RaceAdapterModel>(RaceAdapterModel.QualifyingHeader(showQualifying))
        expected.addAll(expectedQ3Order)

        initSUT(orderBy = QUALIFYING_POS)

        assertValue(Triple(QUALIFYING_POS, expected, expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when view type is qualifying (happy) Q2, items are ordered properly`() = coroutineTest {

        val showQualifying = ShowQualifying(true, true, true, false, false)

        whenever(mockPrefsDB.showQualifyingDelta).thenReturn(false)
        whenever(mockPrefsDB.showGridPenaltiesInQualifying).thenReturn(false)
        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(mockRound1) })
        val expected = mutableListOf<RaceAdapterModel>(RaceAdapterModel.QualifyingHeader(showQualifying))
        expected.addAll(expectedQ2Order)

        initSUT(orderBy = QUALIFYING_POS_2)

        assertValue(Triple(QUALIFYING_POS_2, expected, expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when view type is qualifying (happy) Q1, items are ordered properly`() = coroutineTest {

        val showQualifying = ShowQualifying(true, true, true, false, false)

        whenever(mockPrefsDB.showQualifyingDelta).thenReturn(false)
        whenever(mockPrefsDB.showGridPenaltiesInQualifying).thenReturn(false)
        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(mockRound1) })
        val expected = mutableListOf<RaceAdapterModel>(RaceAdapterModel.QualifyingHeader(showQualifying))
        expected.addAll(expectedQ1Order)

        initSUT(orderBy = QUALIFYING_POS_1)

        assertValue(Triple(QUALIFYING_POS_1, expected, expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when order by is changed list content updates`() = coroutineTest {

        val showQualifying = ShowQualifying(true, true, true, false, false)

        whenever(mockPrefsDB.showQualifyingDelta).thenReturn(false)
        whenever(mockPrefsDB.showGridPenaltiesInQualifying).thenReturn(false)
        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(mockRound1) })

        val expectedQ3 = mutableListOf<RaceAdapterModel>(RaceAdapterModel.QualifyingHeader(showQualifying))
        expectedQ3.addAll(expectedQ3Order)
        val expectedQ2 = mutableListOf<RaceAdapterModel>(RaceAdapterModel.QualifyingHeader(showQualifying))
        expectedQ2.addAll(expectedQ2Order)
        val expectedQ1 = mutableListOf<RaceAdapterModel>(RaceAdapterModel.QualifyingHeader(showQualifying))
        expectedQ1.addAll(expectedQ1Order)

        initSUT(orderBy = QUALIFYING_POS)

        assertLatestValue(Triple(QUALIFYING_POS, expectedQ3, expectedSeasonRound), sut.outputs.raceItems)

        sut.inputs.orderBy(QUALIFYING_POS_1)
        advanceUntilIdle()

        assertLatestValue(Triple(QUALIFYING_POS_1, expectedQ1, expectedSeasonRound), sut.outputs.raceItems)

        sut.inputs.orderBy(QUALIFYING_POS_2)
        advanceUntilIdle()

        assertLatestValue(Triple(QUALIFYING_POS_2, expectedQ2, expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when show qualifying delta is enabled, qualifying delta is supplied`() = coroutineTest {

        val showQualifying = ShowQualifying(true, true, true, true, false)

        whenever(mockPrefsDB.showQualifyingDelta).thenReturn(true)
        whenever(mockPrefsDB.showGridPenaltiesInQualifying).thenReturn(false)
        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(mockRound1) })
        val expected = mutableListOf<RaceAdapterModel>(RaceAdapterModel.QualifyingHeader(showQualifying))
        expected.addAll(expectedQ3OrderWithQualifyingDeltas)

        initSUT(orderBy = QUALIFYING_POS)

        assertValue(Triple(QUALIFYING_POS, expected, expectedSeasonRound), sut.outputs.raceItems)
    }

    @Test
    fun `RaceViewModel when only q1 data is supplied, ordering for multiple qualifying types always does the same order`() = coroutineTest {

        val showQualifying = ShowQualifying(true, false, false, false, false)

        whenever(mockPrefsDB.showQualifyingDelta).thenReturn(false)
        whenever(mockPrefsDB.showGridPenaltiesInQualifying).thenReturn(false)
        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(mockRound3) })
        val expected = mutableListOf<RaceAdapterModel>(RaceAdapterModel.QualifyingHeader(showQualifying))
        expected.addAll(expectedQ3Order(round = mockRound3, showQualifying = showQualifying))

        initSUT(orderBy = QUALIFYING_POS)

        assertLatestValue(Triple(QUALIFYING_POS, expected, SeasonRound(2019, 3)), sut.outputs.raceItems)

        // Not clickable in the UI
//        initSUT(orderBy = QUALIFYING_POS_1)
//        advanceUntilIdle()
//
//        assertLatestValue(Triple(QUALIFYING_POS_1, expected, SeasonRound(2019, 3)), sut.outputs.raceItems)

        initSUT(orderBy = QUALIFYING_POS_2)
        advanceUntilIdle()

        assertLatestValue(Triple(QUALIFYING_POS_2, expected, SeasonRound(2019, 3)), sut.outputs.raceItems)
    }

//    @Test
//    fun `RaceViewModel when only q1 and q2 data is supplied, changing the order adheres to q1, q2 and then true qualifying order`() = coroutineTest {
//
//        TODO("Unit test needs to implemented")
//    }

    @Test
    fun `RaceViewModel initialise emits season round data`() = coroutineTest {

        initSUT()

        sut.inputs.initialise(2020, 1, LocalDate.now())
        advanceUntilIdle()

        assertValue(SeasonRound(2020, 1), sut.outputs.seasonRoundData)
    }

    @AfterEach
    internal fun tearDown() = coroutineTest {

        reset(mockSeasonOverviewDB, mockPrefsDB, mockConnectivityManager)
    }




    //region Round 1 expected qualifying orders

    private val expectedQ3Order: List<RaceAdapterModel> = listOf(
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

    private fun expectedQ3Order(round: Round = mockRound1, showQualifying: ShowQualifying = ShowQualifying(
        q1 = true,
        q2 = true,
        q3 = true,
        penalties = false,
        deltas = false
    )): List<RaceAdapterModel> = listOf(
        convertDriverToSingle(round = round, roundDriver = mockDriver1,
            expectedQualified = 1,
            expectedGrid = 1,
            expectedFinish = 4,
            showQualifying = showQualifying
        ),
        convertDriverToSingle(round = round, roundDriver = mockDriver2,
            expectedQualified = 2,
            expectedGrid = 2,
            expectedFinish = 3,
            showQualifying = showQualifying
        ),
        convertDriverToSingle(round = round, roundDriver = mockDriver3,
            expectedQualified = 3,
            expectedGrid = 4,
            expectedFinish = 2,
            showQualifying = showQualifying
        ),
        convertDriverToSingle(round = round, roundDriver = mockDriver4,
            expectedQualified = 4,
            expectedGrid = 3,
            expectedFinish = 1,
            showQualifying = showQualifying
        )
    )

    private val expectedQ2Order: List<RaceAdapterModel> = listOf(
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
    private val expectedQ1Order: List<RaceAdapterModel> = listOf(
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

    private val expectedQ3OrderWithQualifyingDeltas: List<RaceAdapterModel> = listOf(
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver1,
            expectedQualified = 1,
            expectedGrid = 1,
            expectedFinish = 4,
            expectedQ1Delta = null,
            expectedQ2Delta = "+1.000",
            expectedQ3Delta = null,
            showQualifying = ShowQualifying(true, true, true, true, false)
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver2,
            expectedQualified = 2,
            expectedGrid = 2,
            expectedFinish = 3,
            expectedQ1Delta = "+2.000",
            expectedQ2Delta = null,
            expectedQ3Delta = "+1.000",
            showQualifying = ShowQualifying(true, true, true, true, false)
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver3,
            expectedQualified = 3,
            expectedGrid = 4,
            expectedFinish = 2,
            expectedQ1Delta = "+1.000",
            expectedQ2Delta = "+2.000",
            expectedQ3Delta = null,
            showQualifying = ShowQualifying(true, true, true, true, false)
        ),
        convertDriverToSingle(round = mockRound1, roundDriver = mockDriver4,
            expectedQualified = 4,
            expectedGrid = 3,
            expectedFinish = 1,
            expectedQ1Delta = "+3.000",
            expectedQ2Delta = null,
            expectedQ3Delta = null,
            showQualifying = ShowQualifying(true, true, true, true, false)
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
                                      showQualifying: ShowQualifying = ShowQualifying(
                                          q1 = true,
                                          q2 = true,
                                          q3 = true,
                                          penalties = false,
                                          deltas = false
                                      )
    ): RaceAdapterModel.Single {
        val overview = round.driverOverview(roundDriver.id)
        return RaceAdapterModel.Single(
            season = round.season,
            round = round.round,
            driver = roundDriver,
            q1 = overview.q1,
            q2 = overview.q2,
            q3 = overview.q3,
            race = overview.race?.let { SingleRace(
                points = it.points,
                result = it.time ?: LapTime(),
                pos = expectedFinish,
                gridPos = expectedGrid,
                status = it.status,
                fastestLap = it.fastestLap?.rank == 1
            )},
            qualified = expectedQualified,
            q1Delta = expectedQ1Delta,
            q2Delta = expectedQ2Delta,
            q3Delta = expectedQ3Delta,
            showQualifying = showQualifying
        )
    }
}