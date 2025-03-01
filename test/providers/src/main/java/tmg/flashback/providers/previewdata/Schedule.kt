package tmg.flashback.providers.previewdata

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.model.ScheduleWeather

internal fun Schedule.Companion.model(
    label: String = "label",
    date: LocalDate = LocalDate.of(2020, 1, 1),
    time: LocalTime = LocalTime.of(12, 34),
    weather: ScheduleWeather = ScheduleWeather.model()
): Schedule = Schedule(
    label = label,
    date = date,
    time = time,
    weather = weather
)