package tmg.flashback.statistics.network.models.overview

import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    val label: String,
    val date: String,
    val time: String
) {
    companion object
}