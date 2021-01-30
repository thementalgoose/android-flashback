package tmg.flashback.data.models.stats

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SeasonTest {

    @Test
    fun `Season circuits returns list of circuits`() {
        val expected = listOf(mockCircuitCharlie, mockCircuitDelta)
        assertEquals(expected, mockSeason.circuits)
    }

    @Test
    fun `Season firstRound returns first round`() {
        assertEquals(mockRound1, mockSeason.firstRound)
    }

    @Test
    fun `Season firstRound with no rounds returns null`() {
        assertNull(mockSeason.copy(rounds = emptyList()).firstRound)
    }

    @Test
    fun `Season lastRound returns first round`() {
        assertEquals(mockRound2, mockSeason.lastRound)
    }

    @Test
    fun `Season lastRound with no rounds returns null`() {
        assertNull(mockSeason.copy(rounds = emptyList()).lastRound)
    }

    @Test
    fun `Season constructorStandings calculates standings properly with no constructor data provided`() {

        val driver1Points = mockSeason.rounds.sumBy { it.race[mockDriver1.id]?.points ?: 0 }
        val driver2Points = mockSeason.rounds.sumBy { it.race[mockDriver2.id]?.points ?: 0 }
        val driver3Points = mockSeason.rounds.sumBy { it.race[mockDriver3.id]?.points ?: 0 }
        val driver4Points = mockSeason.rounds.sumBy { it.race[mockDriver4.id]?.points ?: 0 }
        val expected = mapOf(
                mockConstructorAlpha.id to Triple(
                        mockConstructorAlpha,
                        mutableMapOf(
                                mockDriver1.id to Pair(mockDriver1.toDriver(), driver1Points),
                                mockDriver3.id to Pair(mockDriver3.toDriver(), driver3Points)
                        ).toMap(),
                        driver1Points + driver3Points
                ),
                mockConstructorBeta.id to Triple(
                        mockConstructorBeta,
                        mutableMapOf(
                                mockDriver2.id to Pair(mockDriver2.toDriver(), driver2Points),
                                mockDriver4.id to Pair(mockDriver4.toDriver(), driver4Points)
                        ).toMap(),
                        driver2Points + driver4Points
                )
        )

        assertEquals(expected, mockSeason.constructorStandings())
    }

    @Test
    fun `Season constructorStandings calculates standings properly with no standings constructors data provided`() {

        val driver1Points = mockSeason.rounds.sumBy { it.race[mockDriver1.id]?.points ?: 0 }
        val driver2Points = mockSeason.rounds.sumBy { it.race[mockDriver2.id]?.points ?: 0 }
        val driver3Points = mockSeason.rounds.sumBy { it.race[mockDriver3.id]?.points ?: 0 }
        val driver4Points = mockSeason.rounds.sumBy { it.race[mockDriver4.id]?.points ?: 0 }

        val alphaPoints = 20
        val betaPoints = 16

        val expected = mapOf(
                mockConstructorAlpha.id to Triple(
                        mockConstructorAlpha,
                        mutableMapOf(
                                mockDriver1.id to Pair(mockDriver1.toDriver(), driver1Points),
                                mockDriver3.id to Pair(mockDriver3.toDriver(), driver3Points)
                        ).toMap(),
                        alphaPoints
                ),
                mockConstructorBeta.id to Triple(
                        mockConstructorBeta,
                        mutableMapOf(
                                mockDriver2.id to Pair(mockDriver2.toDriver(), driver2Points),
                                mockDriver4.id to Pair(mockDriver4.toDriver(), driver4Points)
                        ).toMap(),
                        betaPoints
                )
        )

        val mockSeasonWithConstructorStandings = mockSeason.copy(
                constructorStandings = mapOf(
                        mockConstructorAlpha.id to Pair(mockConstructorAlpha, alphaPoints),
                        mockConstructorBeta.id to Pair(mockConstructorBeta, betaPoints)
                )
        )

        assertEquals(expected, mockSeasonWithConstructorStandings.constructorStandings())
    }
}