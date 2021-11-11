package tmg.flashback.statistics.network.models.races

import kotlinx.serialization.Serializable

@Serializable
data class InProgressInfo(
    val name: String,
    val round: Int
)