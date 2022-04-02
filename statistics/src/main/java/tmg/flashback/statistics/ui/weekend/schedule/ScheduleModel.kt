package tmg.flashback.statistics.ui.weekend.schedule

import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Schedule

data class ScheduleModel(
    val date: LocalDate,
    val schedules: List<Pair<Schedule, Boolean>> // Schedule, isNotificationSet
)