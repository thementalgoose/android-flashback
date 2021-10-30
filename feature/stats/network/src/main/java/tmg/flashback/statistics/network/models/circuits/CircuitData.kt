package tmg.flashback.statistics.network.models.circuits

import kotlinx.serialization.Serializable

@Serializable
class AllCircuits: HashMap<String, CircuitData>()

@Serializable
data class CircuitData(
    val id: String,
    val name: String,
    val wikiUrl: String?,
    val location: Location?,
    val city: String,
    val country: String,
    val countryISO: String
)