package tmg.flashback.statistics.network.models.constructors

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Constructor(
    val id: String,
    val colour: String,
    val name: String,
    val nationality: String,
    val nationalityISO: String,
    val wikiUrl: String?
) {
    companion object
}