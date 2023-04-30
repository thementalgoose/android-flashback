package tmg.flashback.flashbackapi.api.models.overview

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Event(
    val label: String,
    val date: String,
    val type: String
) {
    companion object
}