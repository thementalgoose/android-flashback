package tmg.flashback.statistics.network.models.circuits

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val lat: String,
    val lng: String
)