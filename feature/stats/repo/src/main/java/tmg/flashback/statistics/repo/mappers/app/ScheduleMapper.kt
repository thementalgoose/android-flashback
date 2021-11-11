package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.Schedule
import tmg.utilities.utils.LocalDateUtils.Companion.fromDate
import tmg.utilities.utils.LocalTimeUtils.Companion.fromTime

class ScheduleMapper {

    fun mapSchedule(schedule: tmg.flashback.statistics.room.models.overview.Schedule?): Schedule? {
        if (schedule == null) return null
        val date = fromDate(schedule.date)
        val time = fromTime(schedule.time)

        if (date != null && time != null) {
            return Schedule(
                label = schedule.label,
                date = date,
                time = time
            )
        }
        return null
    }
}