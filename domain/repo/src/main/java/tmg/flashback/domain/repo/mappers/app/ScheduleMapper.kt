package tmg.flashback.domain.repo.mappers.app

import tmg.flashback.formula1.model.Schedule
import tmg.utilities.utils.LocalDateUtils.Companion.fromDate
import tmg.utilities.utils.LocalTimeUtils.Companion.fromTime
import javax.inject.Inject

class ScheduleMapper @Inject constructor() {

    fun mapSchedule(schedule: tmg.flashback.domain.persistence.models.overview.Schedule?): Schedule? {
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