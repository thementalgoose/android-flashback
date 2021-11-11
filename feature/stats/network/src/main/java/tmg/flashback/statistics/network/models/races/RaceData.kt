package tmg.flashback.statistics.network.models.races

import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.circuits.Circuit

@Serializable
data class RaceData(
    val season: Int,
    val round: Int,
    val name: String,
    val date: String,
    val time: String? = null,
    val circuit: Circuit,
    val wikiUrl: String? = null
) {
    companion object
}