package tmg.flashback.statistics.network.models.circuits

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
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