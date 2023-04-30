package tmg.flashback.statistics.network.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class InProgressInfo(
    val name: String,
    val round: Int
)