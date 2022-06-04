package tmg.flashback.stats.ui.weekend.schedule

import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.model.model
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.weekend.details.DetailsModel
import tmg.utilities.models.StringHolder

fun DetailsModel.ScheduleDay.Companion.model(
    date: LocalDate = LocalDate.of(2020, 1, 1),
    schedules: List<Pair<Schedule, Boolean>> = listOf(
        Schedule.model() to true
    )
): DetailsModel.ScheduleDay = DetailsModel.ScheduleDay(
    date = date,
    schedules = schedules
)

fun DetailsModel.Link.Companion.model(
    label: Int = 0,
    icon: Int = 1,
    url: String = "https://link.com"
): DetailsModel.Link = DetailsModel.Link(
    label = label,
    icon = icon,
    url = url
)

fun DetailsModel.Label.Companion.model(
    label: StringHolder = StringHolder(R.string.details_link_laps, RaceInfo.model().laps ?: ""),
    icon: Int = R.drawable.ic_details_laps,
): DetailsModel.Label = DetailsModel.Label(
    label = label,
    icon = icon
)