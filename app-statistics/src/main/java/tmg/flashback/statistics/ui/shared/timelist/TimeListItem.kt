package tmg.flashback.statistics.ui.shared.timelist

import tmg.flashback.core.model.TimeListDisplayType
import tmg.flashback.core.model.UpNextScheduleTimestamp

data class TimeListItem(
    val type: TimeListDisplayType,
    val itemInList: Int,
    val totalList: Int,
    val item: UpNextScheduleTimestamp
)
