package tmg.flashback.upnext.ui.timelist

import tmg.flashback.upnext.repository.model.TimeListDisplayType
import tmg.flashback.upnext.repository.model.UpNextScheduleTimestamp


data class TimeListItem(
        val type: TimeListDisplayType,
        val itemInList: Int,
        val totalList: Int,
        val item: UpNextScheduleTimestamp
)
