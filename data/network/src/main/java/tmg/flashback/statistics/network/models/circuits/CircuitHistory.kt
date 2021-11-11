package tmg.flashback.statistics.network.models.circuits

import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.constructors.Constructor
import tmg.flashback.statistics.network.models.drivers.Driver

@Serializable
data class CircuitHistory(
    val data: Circuit,
    val results: Map<String, CircuitResult>?
) {
    companion object
}

@Serializable
data class CircuitResult(
    val race: CircuitResultRace,
    val preview: List<CircuitPreviewPosition>?
) {
    companion object
}

@Serializable
data class CircuitResultRace(
    val season: Int,
    val round: Int,
    val name: String,
    val date: String,
    val time: String? = null,
    val wikiUrl: String? = null,
) {
    companion object
}

@Serializable
data class CircuitPreviewPosition(
    val position: Int,
    val driver: Driver,
    val constructor: Constructor
) {
    companion object
}