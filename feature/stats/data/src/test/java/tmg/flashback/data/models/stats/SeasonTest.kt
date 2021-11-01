package tmg.flashback.data.models.stats

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.constructorStandings

class SeasonTest {

    @Test
    fun `circuits returns list of circuits`() {
        val expected = listOf(mockCircuitCharlie, mockCircuitDelta)
        assertEquals(expected, mockSeason.circuits)
    }

    @Test
    fun `firstRound returns first round`() {
        assertEquals(mockRound1, mockSeason.firstRound)
    }

    @Test
    fun `firstRound with no rounds returns null`() {
        assertNull(mockSeason.copy(rounds = emptyList()).firstRound)
    }

    @Test
    fun `lastRound returns first round`() {
        assertEquals(mockRound2, mockSeason.lastRound)
    }

    @Test
    fun `lastRound with no rounds returns null`() {
        assertNull(mockSeason.copy(rounds = emptyList()).lastRound)
    }

    @Test
    fun `constructorStandings calculates standings properly with no constructor data provided`() {

        val driver1Points = mockSeason.rounds.sumOf { it.race[MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id]?.points ?: 0.0 }
        val driver2Points = mockSeason.rounds.sumOf { it.race[mockDriver2.id]?.points ?: 0.0 }
        val driver3Points = mockSeason.rounds.sumOf { it.race[mockDriver3.id]?.points ?: 0.0 }
        val driver4Points = mockSeason.rounds.sumOf { it.race[mockDriver4.id]?.points ?: 0.0 }
        val expected = mapOf(
                mockConstructorAlpha.id to Triple(
                        mockConstructorAlpha,
                        mutableMapOf(
                                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id to Pair(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(1), driver1Points),
                                mockDriver3.id to Pair(mockDriver3.toConstructorDriver(1), driver3Points)
                        ).toMap(),
                        driver1Points + driver3Points
                ),
                mockConstructorBeta.id to Triple(
                        mockConstructorBeta,
                        mutableMapOf(
                                mockDriver2.id to Pair(mockDriver2.toConstructorDriver(1), driver2Points),
                                mockDriver4.id to Pair(mockDriver4.toConstructorDriver(1), driver4Points)
                        ).toMap(),
                        driver2Points + driver4Points
                )
        )

        assertEquals(expected, mockSeason.constructorStandings())
    }

    @Test
    fun `constructorStandings calculates standings properly with no standings constructors data provided`() {

        val driver1Points = mockSeason.rounds.sumOf { it.race[MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id]?.points ?: 0.0 }
        val driver2Points = mockSeason.rounds.sumOf { it.race[mockDriver2.id]?.points ?: 0.0 }
        val driver3Points = mockSeason.rounds.sumOf { it.race[mockDriver3.id]?.points ?: 0.0 }
        val driver4Points = mockSeason.rounds.sumOf { it.race[mockDriver4.id]?.points ?: 0.0 }

        val alphaPoints = 20.0
        val betaPoints = 16.0

        val expected = mapOf(
                mockConstructorAlpha.id to Triple(
                        mockConstructorAlpha,
                        mutableMapOf(
                                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id to Pair(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(1), driver1Points),
                                mockDriver3.id to Pair(mockDriver3.toConstructorDriver(1), driver3Points)
                        ).toMap(),
                        alphaPoints
                ),
                mockConstructorBeta.id to Triple(
                        mockConstructorBeta,
                        mutableMapOf(
                                mockDriver2.id to Pair(mockDriver2.toConstructorDriver(1), driver2Points),
                                mockDriver4.id to Pair(mockDriver4.toConstructorDriver(1), driver4Points)
                        ).toMap(),
                        betaPoints
                )
        )

        val mockSeasonWithConstructorStandings = mockSeason.copy(
                constructorStandings = listOf(
                    tmg.flashback.formula1.model.SeasonStanding<tmg.flashback.formula1.model.Constructor>(
                        mockConstructorAlpha,
                        alphaPoints,
                        if (alphaPoints > betaPoints) 1 else 2
                    ),
                    tmg.flashback.formula1.model.SeasonStanding<tmg.flashback.formula1.model.Constructor>(
                        mockConstructorBeta,
                        betaPoints,
                        if (alphaPoints > betaPoints) 1 else 2
                    )
                )
        )

        assertEquals(expected, mockSeasonWithConstructorStandings.constructorStandings())
    }
}