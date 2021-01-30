package tmg.flashback.core.model

data class UpNextSchedule(
    val season: Int,
    val round: Int,
    val name: String,
    val timestamp: Timestamp,
    val flag: String?,
    val circuitId: String?,
    val circuitName: String?
)

