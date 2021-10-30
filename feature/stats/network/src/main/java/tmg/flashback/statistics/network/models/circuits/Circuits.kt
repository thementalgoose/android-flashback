package tmg.flashback.statistics.network.models.circuits

import tmg.flashback.statistics.network.models.constructors.ConstructorData
import tmg.flashback.statistics.network.models.drivers.DriverData

data class Circuits(
    val data: CircuitData,
    val results: Map<String, CircuitResult>
)

data class CircuitResult(
    val race: CircuitResultRace,
    val preview: List<CircuitPreviewPosition>
)

data class CircuitResultRace(
    val season: Int,
    val round: Int,
    val name: String,
    val date: String,
    val wikiUrl: String? = null,
)

data class CircuitPreviewPosition(
    val position: Int,
    val driver: DriverData,
    val constructor: ConstructorData
)