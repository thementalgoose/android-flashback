package tmg.flashback.statistics.ui.shared.timelist

import tmg.flashback.core.model.UpNextScheduleTimestamp

data class TimeListItem(
    val type: TimeListDisplayType,
    val item: UpNextScheduleTimestamp
)

enum class TimeListDisplayType {
    UTC,
    LOCAL,
    RELATIVE
}