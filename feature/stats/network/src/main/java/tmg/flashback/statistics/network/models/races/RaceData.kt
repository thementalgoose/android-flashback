package tmg.flashback.statistics.network.models.races

import tmg.flashback.statistics.network.models.circuits.CircuitData

data class RaceData(
    val season: Int,
    val round: Int,
    val name: String,
    val date: String,
    val circuit: CircuitData,
    val wikiUrl: String?
)