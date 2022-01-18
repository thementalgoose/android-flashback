package tmg.flashback.statistics.network.models.drivers

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Driver(
    val id: String,
    val firstName: String,
    val lastName: String,
    val dob: String,
    val nationality: String,
    val nationalityISO: String,
    val photoUrl: String? = null,
    val wikiUrl: String? = null,
    val code: String? = null,
    val permanentNumber: String? = null
) {
    companion object
}