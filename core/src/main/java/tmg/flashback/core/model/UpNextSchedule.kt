package tmg.flashback.core.model

data class UpNextSchedule(
    val season: Int,
    val round: Int,
    val title: String,
    val subtitle: String?,
    val values: List<UpNextScheduleTimestamp>,
    val flag: String?,
    val circuitId: String?
)

data class UpNextScheduleTimestamp(
    val label: String,
    val timestamp: Timestamp
)

enum class TimeListDisplayType {
    UTC,
    LOCAL,
    RELATIVE
}