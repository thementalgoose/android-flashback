package tmg.flashback.statistics.network.models.overview

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Schedule(
    val label: String,
    val date: String,
    val time: String
) {
    companion object
}