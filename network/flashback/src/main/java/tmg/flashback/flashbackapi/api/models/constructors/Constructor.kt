package tmg.flashback.flashbackapi.api.models.constructors

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Constructor(
    val id: String,
    val colour: String,
    val name: String,
    val photoUrl: String?,
    val nationality: String,
    val nationalityISO: String,
    val wikiUrl: String?
) {
    companion object
}