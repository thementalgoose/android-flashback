package tmg.flashback.stats.ui.weekend.schedule

import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.model.model

fun ScheduleModel.Companion.model(
    date: LocalDate = LocalDate.of(2020, 1, 1),
    schedules: List<Pair<Schedule, Boolean>> = listOf(
        Schedule.model() to true
    )
): ScheduleModel = ScheduleModel(
    date = date,
    schedules = schedules
)