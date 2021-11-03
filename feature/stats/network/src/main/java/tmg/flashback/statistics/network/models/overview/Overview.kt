package tmg.flashback.statistics.network.models.overview

import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.circuits.Circuit
import tmg.flashback.statistics.network.models.circuits.CircuitData

typealias Overview = Map<String, OverviewRace>

@Serializable
data class OverviewRace(
    val season: Int,
    val round: Int,
    val name: String,
    val circuit: CircuitData,
    val date: String,
    val time: String? = null,
    val hasQualifying: Boolean,
    val hasRace: Boolean
) {
    companion object
}