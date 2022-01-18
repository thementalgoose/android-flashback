package tmg.flashback.statistics.network.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class FastestLap(
    val position: Int,
    val time: String
) {
    companion object
}