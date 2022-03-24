package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

fun Schedule.Companion.model(
    label: String = "label",
    date: LocalDate = LocalDate.of(2020, 1, 1),
    time: LocalTime = LocalTime.of(12, 34)
): Schedule = Schedule(
    label = label,
    date = date,
    time = time
)