package tmg.flashback.statistics.ui.shared.timelist

import tmg.configuration.repository.models.TimeListDisplayType
import tmg.configuration.repository.models.UpNextScheduleTimestamp

data class TimeListItem(
    val type: TimeListDisplayType,
    val itemInList: Int,
    val totalList: Int,
    val item: UpNextScheduleTimestamp
)
