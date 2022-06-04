package tmg.flashback.stats.ui.weekend.schedule

import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.model.model
import tmg.flashback.stats.ui.weekend.details.DetailsModel

fun DetailsModel.Companion.model(
    date: LocalDate = LocalDate.of(2020, 1, 1),
    schedules: List<Pair<Schedule, Boolean>> = listOf(
        Schedule.model() to true
    )
): DetailsModel = DetailsModel(
    date = date,
    schedules = schedules
)