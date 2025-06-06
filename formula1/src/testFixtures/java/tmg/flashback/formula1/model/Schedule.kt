package tmg.flashback.formula1.model

import java.time.LocalDate
import java.time.LocalTime

fun Schedule.Companion.model(
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