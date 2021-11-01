package tmg.flashback.statistics.network.models.circuits

import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.constructors.ConstructorData
import tmg.flashback.statistics.network.models.drivers.DriverData

@Serializable
data class Circuit(
    val data: CircuitData,
    val results: Map<String, CircuitResult>?
)

@Serializable
data class CircuitResult(
    val race: CircuitResultRace,
    val preview: List<CircuitPreviewPosition>?
)

@Serializable
data class CircuitResultRace(
    val season: Int,
    val round: Int,
    val name: String,
    val date: String,
    val time: String? = null,
    val wikiUrl: String? = null,
)

@Serializable
data class CircuitPreviewPosition(
    val position: Int,
    val driver: DriverData,
    val constructor: ConstructorData
)