package tmg.flashback.data.models.remoteconfig

import tmg.flashback.data.models.Timestamp

data class UpNextSchedule(
    val season: Int,
    val round: Int,
    val name: String,
    val timestamp: Timestamp,
    val flag: String?,
    val circuitId: String?,
    val circuitName: String?
)