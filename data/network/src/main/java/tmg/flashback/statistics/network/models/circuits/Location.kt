package tmg.flashback.statistics.network.models.circuits

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Location(
    val lat: String,
    val lng: String
) {
    companion object
}