package tmg.flashback.statistics.network.models.races

import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.circuits.CircuitData

@Serializable
data class RaceData(
    val season: Int,
    val round: Int,
    val name: String,
    val date: String,
    val time: String?,
    val circuit: CircuitData,
    val wikiUrl: String?
)