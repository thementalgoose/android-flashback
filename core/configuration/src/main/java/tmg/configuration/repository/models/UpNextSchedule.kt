package tmg.configuration.repository.models

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

// TODO: Move this to up next feature module
enum class TimeListDisplayType {
    UTC,
    LOCAL,
    RELATIVE
}