package tmg.flashback.statistics.network.models.overview

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