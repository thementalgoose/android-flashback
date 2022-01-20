package tmg.flashback.statistics.network.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class WinterTesting(
    val label: String,
    val date: String
) {
    companion object
}