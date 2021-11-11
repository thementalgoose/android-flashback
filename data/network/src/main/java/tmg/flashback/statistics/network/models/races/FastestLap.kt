package tmg.flashback.statistics.network.models.races

import kotlinx.serialization.Serializable

@Serializable
data class FastestLap(
    val position: Int,
    val time: String
) {
    companion object
}