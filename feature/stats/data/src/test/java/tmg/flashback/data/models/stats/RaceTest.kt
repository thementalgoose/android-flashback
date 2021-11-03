package tmg.flashback.data.models.stats

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.allPoints
import tmg.flashback.formula1.model.bestFinish
import tmg.flashback.formula1.model.bestQualified
import tmg.flashback.formula1.model.bestQualifyingResultFor
import tmg.flashback.formula1.model.bestRaceResultFor
import tmg.flashback.formula1.model.constructorStandings
import tmg.flashback.formula1.model.driverStandings
import tmg.flashback.formula1.model.maxConstructorPointsInSeason

class RaceTest {

    @Test
    fun `driverOverview returns driver overview with relevant qualifying result for driver`() {

        assertEquals(expectedDriver1, MOCK_RACE_1.driverOverview(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id))
        assertEquals(expectedDriver2, MOCK_RACE_1.driverOverview(mockDriver2.id))
        assertEquals(expectedDriver3, MOCK_RACE_1.driverOverview(mockDriver3.id))
        assertEquals(expectedDriver4, MOCK_RACE_1.driverOverview(mockDriver4.id))
    }

    @Test
    fun `has sprint qualifying when sprint quali results are not empty`() {

        val sprintQualiRound = MOCK_RACE_1.copy(
            qSprint = mapOf(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id to tmg.flashback.formula1.model.RaceSprintQualifyingResult(
                driver = round1RaceResultDriver1.driver,
                time = round1RaceResultDriver1.time,
                points = round1RaceResultDriver1.points,
                grid = round1RaceResultDriver1.grid,
                qualified = round1RaceResultDriver1.qualified,
                finish = round1RaceResultDriver1.finish,
                status = round1RaceResultDriver1.status,
            )
            )
        )
        assertTrue(sprintQualiRound.hasSprintQualifying)
    }

    @Test
    fun `does not have sprint qualifying when sprint quali results are empty`() {

        val sprintQualiRound = MOCK_RACE_1.copy(qSprint = emptyMap())
        assertFalse(sprintQualiRound.hasSprintQualifying)
    }

    @Test
    fun `fastest laps in q1, q2 and q3 return the fastest lap`() {

        assertEquals(tmg.flashback.formula1.model.LapTime(0, 1, 1, 0), MOCK_RACE_1.q1FastestLap)
        assertEquals(tmg.flashback.formula1.model.LapTime(0, 1, 1, 0), MOCK_RACE_1.q2FastestLap)
        assertEquals(tmg.flashback.formula1.model.LapTime(0, 1, 1, 0), MOCK_RACE_1.q3FastestLap)
    }

    @Test
    fun `constructorStandings returns correct standings`() {

        val expected = listOf(
            tmg.flashback.formula1.model.RaceConstructorStandings(
                constructor = mockConstructorAlpha,
                points = 20.0
            ), // driver 1 + 3 as they have current constructor as mockConstructorAlpha
            tmg.flashback.formula1.model.RaceConstructorStandings(
                constructor = mockConstructorBeta,
                points = 30.0
            ) // driver 2 + 4 as they have current constructor as mockConstructorBeta
        )
        assertEquals(expected, MOCK_RACE_1.constructorStandings)
    }

    @Test
    fun `driverStandings returns correct driver standings`() {

        val expected = listOf(
            tmg.flashback.formula1.model.RoundDriverStandings(
                driver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(
                    1
                ), points = 5.0
            ),
            tmg.flashback.formula1.model.RoundDriverStandings(
                driver = mockDriver2.toConstructorDriver(
                    1
                ), points = 10.0
            ),
            tmg.flashback.formula1.model.RoundDriverStandings(
                driver = mockDriver3.toConstructorDriver(
                    1
                ), points = 15.0
            ),
            tmg.flashback.formula1.model.RoundDriverStandings(
                driver = mockDriver4.toConstructorDriver(
                    1
                ), points = 20.0
            )
        )
        assertEquals(expected, MOCK_RACE_1.driverStandings)
    }

    @Test
    fun `(List) constructorStandings calculates constructor standings for season overview`() {

        val expected = mapOf(
                "alpha" to Triple(
                        mockConstructorAlpha,
                        mapOf(
                                "1" to Pair(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(1), 21.0),
                                "3" to Pair(mockDriver3.toConstructorDriver(1), 27.0)
                        ),
                        48.0
                ),
                "beta" to Triple(
                        mockConstructorBeta,
                        mapOf(
                                "4" to Pair(mockDriver4.toConstructorDriver(1), 24.0),
                                "2" to Pair(mockDriver2.toConstructorDriver(1), 18.0)
                        ),
                        42.0
                )
        )
        assertEquals(expected, mockSeason.constructorStandings())
    }

    @Test
    fun `(List) constructorStandings maxDriverPointsInSeason returns the most points `() {

        val example = mapOf(
                "alpha" to Triple(mockConstructorAlpha, mapOf(
                        "1" to Pair(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(), 21.0),
                        "3" to Pair(mockDriver3.toConstructorDriver(), 27.0)
                ), 48.0),
                "beta" to Triple(mockConstructorBeta, mapOf(
                        "4" to Pair(mockDriver4.toConstructorDriver(), 24.0),
                        "2" to Pair(mockDriver2.toConstructorDriver(), 18.0)
                ), 42.0)
        )
        assertEquals(48.0, example.maxConstructorPointsInSeason())
    }

    @Test
    fun `(List) driverStandings calculates driver standings for season overview`() {

        val driverStandings = mapOf(
                "1" to Pair(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(1), 21.0),
                "2" to Pair(mockDriver2.toConstructorDriver(1), 18.0),
                "3" to Pair(mockDriver3.toConstructorDriver(1), 27.0),
                "4" to Pair(mockDriver4.toConstructorDriver(1), 24.0)
        )
        assertEquals(driverStandings, listOf(MOCK_RACE_1, MOCK_RACE_2).driverStandings())
    }

    @Test
    fun `(List) allPoints for constructorStandings individual item returns the sum of the points`() {

        val example = mapOf(
                "1" to Pair(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1, 21),
                "3" to Pair(mockDriver3, 27)
        )
        assertEquals(48, example.allPoints())
    }

    @Test
    fun `(List) bestQualified for a driver id returns their best qualifying position`() {

        assertEquals(1, listOf(MOCK_RACE_1, MOCK_RACE_2).bestQualified(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id))
        assertEquals(2, listOf(MOCK_RACE_1, MOCK_RACE_2).bestQualified(mockDriver2.id))
        assertEquals(3, listOf(MOCK_RACE_1, MOCK_RACE_2).bestQualified(mockDriver3.id))
        assertEquals(1, listOf(MOCK_RACE_1, MOCK_RACE_2).bestQualified(mockDriver4.id))
    }

    @Test
    fun `(List) bestQualifyingResultFor for a driver id returns their best qualifying position`() {

        assertEquals(Pair(1, listOf(MOCK_RACE_1)), listOf(MOCK_RACE_1, MOCK_RACE_2).bestQualifyingResultFor(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id))
        assertEquals(Pair(2, listOf(MOCK_RACE_1)), listOf(MOCK_RACE_1, MOCK_RACE_2).bestQualifyingResultFor(mockDriver2.id))
        assertEquals(Pair(3, listOf(MOCK_RACE_1, MOCK_RACE_2)), listOf(MOCK_RACE_1, MOCK_RACE_2).bestQualifyingResultFor(mockDriver3.id)) // Grid penalties not applied to qualifying position
        assertEquals(Pair(1, listOf(MOCK_RACE_2)), listOf(MOCK_RACE_1, MOCK_RACE_2).bestQualifyingResultFor(mockDriver4.id))
    }

    @Test
    fun `(List) bestFinish for a driver id returns their best finish position`() {

        assertEquals(1, listOf(MOCK_RACE_1, MOCK_RACE_2).bestFinish(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id))
        assertEquals(3, listOf(MOCK_RACE_1, MOCK_RACE_2).bestFinish(mockDriver2.id))
        assertEquals(2, listOf(MOCK_RACE_1, MOCK_RACE_2).bestFinish(mockDriver3.id))
        assertEquals(1, listOf(MOCK_RACE_1, MOCK_RACE_2).bestFinish(mockDriver4.id))
    }

    @Test
    fun `(List) bestRaceResultFor for a driver id returns their best finish position`() {

        assertEquals(Pair(1, listOf(MOCK_RACE_2)), listOf(MOCK_RACE_1, MOCK_RACE_2).bestRaceResultFor(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id))
        assertEquals(Pair(3, listOf(MOCK_RACE_1, MOCK_RACE_2)), listOf(MOCK_RACE_1, MOCK_RACE_2).bestRaceResultFor(mockDriver2.id))
        assertEquals(Pair(2, listOf(MOCK_RACE_1, MOCK_RACE_2)), listOf(MOCK_RACE_1, MOCK_RACE_2).bestRaceResultFor(mockDriver3.id)) // Grid penalties not applied to qualifying position
        assertEquals(Pair(1, listOf(MOCK_RACE_1)), listOf(MOCK_RACE_1, MOCK_RACE_2).bestRaceResultFor(mockDriver4.id))
    }

    private val expectedDriver1: tmg.flashback.formula1.model.RaceDriverOverview =
        tmg.flashback.formula1.model.RaceDriverOverview(
            q1 = mockRound1Driver1Q1,
            q2 = mockRound1Driver1Q2,
            q3 = mockRound1Driver1Q3,
            qSprint = null,
            race = round1RaceResultDriver1
        )
    private val expectedDriver2: tmg.flashback.formula1.model.RaceDriverOverview =
        tmg.flashback.formula1.model.RaceDriverOverview(
            q1 = mockRound1Driver2Q1,
            q2 = mockRound1Driver2Q2,
            q3 = mockRound1Driver2Q3,
            qSprint = null,
            race = round1RaceResultDriver2
        )
    private val expectedDriver3: tmg.flashback.formula1.model.RaceDriverOverview =
        tmg.flashback.formula1.model.RaceDriverOverview(
            q1 = mockRound1Driver3Q1,
            q2 = mockRound1Driver3Q2,
            q3 = null,
            qSprint = null,
            race = round1RaceResultDriver3
        )
    private val expectedDriver4: tmg.flashback.formula1.model.RaceDriverOverview =
        tmg.flashback.formula1.model.RaceDriverOverview(
            q1 = mockRound1Driver4Q1,
            q2 = null,
            q3 = null,
            qSprint = null,
            race = round1RaceResultDriver4
        )
}