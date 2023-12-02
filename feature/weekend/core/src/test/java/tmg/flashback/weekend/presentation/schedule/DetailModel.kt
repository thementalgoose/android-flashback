package tmg.flashback.weekend.presentation.schedule

import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.model.model
import tmg.flashback.weekend.R
import tmg.flashback.ui.R.drawable
import tmg.flashback.strings.R.string
import tmg.flashback.weekend.presentation.details.DetailsModel
import tmg.utilities.models.StringHolder

fun DetailsModel.ScheduleWeekend.Companion.model(
    days: List<Pair<LocalDate, List<Pair<Schedule, Boolean>>>> = listOf(
        LocalDate.of(2020, 1, 1) to listOf(
            Schedule.model() to true
        )
    ),
    temperatureMetric: Boolean = true,
    windspeedMetric: Boolean = false
): DetailsModel.ScheduleWeekend = DetailsModel.ScheduleWeekend(
    days = days,
    temperatureMetric = temperatureMetric,
    windspeedMetric = windspeedMetric
)

fun DetailsModel.Link.Companion.url(
    label: Int = 0,
    icon: Int = 1,
    url: String = "https://link.com"
): DetailsModel.Link = DetailsModel.Link.Url(
    label = label,
    icon = icon,
    url = url
)

fun DetailsModel.Track.Companion.model(
    circuit: Circuit = Circuit.model(),
    raceName: String = RaceInfo.model().name,
    season: Int = RaceInfo.model().season,
    laps: String? = RaceInfo.model().laps
): DetailsModel.Track = DetailsModel.Track(
    circuit = circuit,
    raceName = raceName,
    season = season,
    laps = laps
)

fun DetailsModel.Label.Companion.model(
    label: StringHolder = StringHolder(string.details_link_laps, RaceInfo.model().laps ?: ""),
    icon: Int = drawable.ic_details_laps,
): DetailsModel.Label = DetailsModel.Label(
    label = label,
    icon = icon
)