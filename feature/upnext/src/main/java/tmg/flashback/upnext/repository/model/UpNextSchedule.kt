package tmg.flashback.upnext.repository.model

import tmg.flashback.formula1.model.Timestamp

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