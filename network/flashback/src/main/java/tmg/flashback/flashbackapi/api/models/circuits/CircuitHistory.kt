package tmg.flashback.flashbackapi.api.models.circuits

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import tmg.flashback.flashbackapi.api.models.constructors.Constructor
import tmg.flashback.flashbackapi.api.models.drivers.Driver

@Keep
@Serializable
data class CircuitHistory(
    val data: Circuit,
    val results: Map<String, CircuitResult>?
) {
    companion object
}

@Keep
@Serializable
data class CircuitResult(
    val race: CircuitResultRace,
    val preview: List<CircuitPreviewPosition>?
) {
    companion object
}

@Keep
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

@Keep
@Serializable
data class CircuitPreviewPosition(
    val position: Int,
    val driver: Driver,
    val constructor: Constructor
) {
    companion object
}