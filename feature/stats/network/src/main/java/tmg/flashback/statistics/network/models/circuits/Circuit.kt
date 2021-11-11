package tmg.flashback.statistics.network.models.circuits

import kotlinx.serialization.Serializable

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