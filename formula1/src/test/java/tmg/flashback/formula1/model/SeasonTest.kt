package tmg.flashback.formula1.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SeasonTest {

    @Test
    fun `circuits returns list of all circuits`() {
        val input = Season.model(races = listOf(
            Race.model(raceInfo = RaceInfo.model(circuit = Circuit.model(
                id = "id1"
            ))),
            Race.model(raceInfo = RaceInfo.model(circuit = Circuit.model(
                id = "id1"
            ))),
            Race.model(raceInfo = RaceInfo.model(circuit = Circuit.model(
                id = "id2"
            )))
        ))

        assertEquals(listOf("id1", "id2"), input.circuits.map { it.id })
    }

    @Test
    fun `first race returns race with min round`() {
        val input = Season.model(
            races = listOf(
                Race.model(raceInfo = RaceInfo.model(round = 3, name = "min")),
                Race.model(raceInfo = RaceInfo.model(round = 4, name = "max"))
            )
        )

        assertEquals(3, input.firstRace!!.raceInfo.round)
    }

    @Test
    fun `last race returns race with max round`() {
        val input = Season.model(
            races = listOf(
                Race.model(raceInfo = RaceInfo.model(round = 3, name = "min")),
                Race.model(raceInfo = RaceInfo.model(round = 4, name = "max"))
            )
        )

        assertEquals(4, input.lastRace!!.raceInfo.round)
    }
}