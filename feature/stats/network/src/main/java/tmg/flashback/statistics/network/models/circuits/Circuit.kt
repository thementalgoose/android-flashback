package tmg.flashback.statistics.network.models.circuits

import kotlinx.serialization.Serializable

typealias AllCircuits = Map<String, Circuit>

@Serializable
data class Circuit(
    val id: String,
    val name: String,
    val wikiUrl: String?,
    val location: Location?,
    val city: String,
    val country: String,
    val countryISO: String
) {
    companion object
}