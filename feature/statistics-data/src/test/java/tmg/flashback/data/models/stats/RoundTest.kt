package tmg.flashback.data.models.stats

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RoundTest {

    @Test
    fun `driverOverview returns driver overview with relevant qualifying result for driver`() {

        assertEquals(expectedDriver1, mockRound1.driverOverview(mockDriver1.id))
        assertEquals(expectedDriver2, mockRound1.driverOverview(mockDriver2.id))
        assertEquals(expectedDriver3, mockRound1.driverOverview(mockDriver3.id))
        assertEquals(expectedDriver4, mockRound1.driverOverview(mockDriver4.id))
    }

    @Test
    fun `has sprint qualifying when sprint quali results are not empty`() {

        val sprintQualiRound = mockRound1.copy(
            qSprint = mapOf(mockDriver1.id to RoundSprintQualifyingResult(
                driver = round1RaceResultDriver1.driver,
                time = round1RaceResultDriver1.time,
                points = round1RaceResultDriver1.points,
                grid = round1RaceResultDriver1.grid,
                qualified = round1RaceResultDriver1.qualified,
                finish = round1RaceResultDriver1.finish,
                status = round1RaceResultDriver1.status,
            ))
        )
        assertTrue(sprintQualiRound.hasSprintQualifying)
    }

    @Test
    fun `does not have sprint qualifying when sprint quali results are empty`() {

        val sprintQualiRound = mockRound1.copy(qSprint = emptyMap())
        assertFalse(sprintQualiRound.hasSprintQualifying)
    }

    @Test
    fun `fastest laps in q1, q2 and q3 return the fastest lap`() {

        assertEquals(LapTime(0, 1, 1, 0), mockRound1.q1FastestLap)
        assertEquals(LapTime(0, 1, 1, 0), mockRound1.q2FastestLap)
        assertEquals(LapTime(0, 1, 1, 0), mockRound1.q3FastestLap)
    }

    @Test
    fun `constructorStandings returns correct standings`() {

        val expected = listOf(
                RoundConstructorStandings(constructor = mockConstructorAlpha, points = 20), // driver 1 + 3 as they have current constructor as mockConstructorAlpha
                RoundConstructorStandings(constructor = mockConstructorBeta, points = 30) // driver 2 + 4 as they have current constructor as mockConstructorBeta
        )
        assertEquals(expected, mockRound1.constructorStandings)
    }

    @Test
    fun `driverStandings returns correct driver standings`() {

        val expected = listOf(
                RoundDriverStandings(driver = mockDriver1, points = 5),
                RoundDriverStandings(driver = mockDriver2, points = 10),
                RoundDriverStandings(driver = mockDriver3, points = 15),
                RoundDriverStandings(driver = mockDriver4, points = 20)
        )
        assertEquals(expected, mockRound1.driverStandings)
    }

    @Test
    fun `(List) constructorStandings calculates constructor standings for season overview`() {

        val expected = mapOf(
                "alpha" to Triple(
                        mockConstructorAlpha,
                        mapOf(
                                "1" to Pair(mockDriver1.toDriver(), 21),
                                "3" to Pair(mockDriver3.toDriver(), 27)
                        ),
                        48
                ),
                "beta" to Triple(
                        mockConstructorBeta,
                        mapOf(
                                "4" to Pair(mockDriver4.toDriver(), 24),
                                "2" to Pair(mockDriver2.toDriver(), 18)
                        ),
                        42
                )
        )
        assertEquals(expected, mockSeason.constructorStandings())
    }

    @Test
    fun `(List) constructorStandings maxDriverPointsInSeason returns the most points `() {

        val example = mapOf(
                "alpha" to Triple(mockConstructorAlpha, mapOf(
                        "1" to Pair(mockDriver1.toDriver(), 21),
                        "3" to Pair(mockDriver3.toDriver(), 27)
                ), 48),
                "beta" to Triple(mockConstructorBeta, mapOf(
                        "4" to Pair(mockDriver4.toDriver(), 24),
                        "2" to Pair(mockDriver2.toDriver(), 18)
                ), 42)
        )
        assertEquals(48, example.maxConstructorPointsInSeason())
    }

    @Test
    fun `(List) driverStandings calculates driver standings for season overview`() {

        val driverStandings = mapOf(
                "1" to Pair(mockDriver1, 21),
                "2" to Pair(mockDriver2, 18),
                "3" to Pair(mockDriver3, 27),
                "4" to Pair(mockDriver4, 24)
        )
        assertEquals(driverStandings, listOf(mockRound1, mockRound2).driverStandings())
    }

    @Test
    fun `(List) allPoints for constructorStandings individual item returns the sum of the points`() {

        val example = mapOf(
                "1" to Pair(mockDriver1.toDriver(), 21),
                "3" to Pair(mockDriver3.toDriver(), 27)
        )
        assertEquals(48, example.allPoints())
    }

    @Test
    fun `(List) bestQualified for a driver id returns their best qualifying position`() {

        assertEquals(1, listOf(mockRound1, mockRound2).bestQualified(mockDriver1.id))
        assertEquals(2, listOf(mockRound1, mockRound2).bestQualified(mockDriver2.id))
        assertEquals(3, listOf(mockRound1, mockRound2).bestQualified(mockDriver3.id))
        assertEquals(1, listOf(mockRound1, mockRound2).bestQualified(mockDriver4.id))
    }

    @Test
    fun `(List) bestQualifyingResultFor for a driver id returns their best qualifying position`() {

        assertEquals(Pair(1, listOf(mockRound1)), listOf(mockRound1, mockRound2).bestQualifyingResultFor(mockDriver1.id))
        assertEquals(Pair(2, listOf(mockRound1)), listOf(mockRound1, mockRound2).bestQualifyingResultFor(mockDriver2.id))
        assertEquals(Pair(3, listOf(mockRound1, mockRound2)), listOf(mockRound1, mockRound2).bestQualifyingResultFor(mockDriver3.id)) // Grid penalties not applied to qualifying position
        assertEquals(Pair(1, listOf(mockRound2)), listOf(mockRound1, mockRound2).bestQualifyingResultFor(mockDriver4.id))
    }

    @Test
    fun `(List) bestFinish for a driver id returns their best finish position`() {

        assertEquals(1, listOf(mockRound1, mockRound2).bestFinish(mockDriver1.id))
        assertEquals(3, listOf(mockRound1, mockRound2).bestFinish(mockDriver2.id))
        assertEquals(2, listOf(mockRound1, mockRound2).bestFinish(mockDriver3.id))
        assertEquals(1, listOf(mockRound1, mockRound2).bestFinish(mockDriver4.id))
    }

    @Test
    fun `(List) bestRaceResultFor for a driver id returns their best finish position`() {

        assertEquals(Pair(1, listOf(mockRound2)), listOf(mockRound1, mockRound2).bestRaceResultFor(mockDriver1.id))
        assertEquals(Pair(3, listOf(mockRound1, mockRound2)), listOf(mockRound1, mockRound2).bestRaceResultFor(mockDriver2.id))
        assertEquals(Pair(2, listOf(mockRound1, mockRound2)), listOf(mockRound1, mockRound2).bestRaceResultFor(mockDriver3.id)) // Grid penalties not applied to qualifying position
        assertEquals(Pair(1, listOf(mockRound1)), listOf(mockRound1, mockRound2).bestRaceResultFor(mockDriver4.id))
    }

    private val expectedDriver1: RoundDriverOverview = RoundDriverOverview(q1 = mockRound1Driver1Q1, q2 = mockRound1Driver1Q2, q3 = mockRound1Driver1Q3, race = round1RaceResultDriver1)
    private val expectedDriver2: RoundDriverOverview = RoundDriverOverview(q1 = mockRound1Driver2Q1, q2 = mockRound1Driver2Q2, q3 = mockRound1Driver2Q3, race = round1RaceResultDriver2)
    private val expectedDriver3: RoundDriverOverview = RoundDriverOverview(q1 = mockRound1Driver3Q1, q2 = mockRound1Driver3Q2, q3 = null, race = round1RaceResultDriver3)
    private val expectedDriver4: RoundDriverOverview = RoundDriverOverview(q1 = mockRound1Driver4Q1, q2 = null, q3 = null, race = round1RaceResultDriver4)
}