package tmg.flashback.providers.model

import tmg.flashback.formula1.model.LapTime

fun LapTime.Companion.model(
    hours: Int = 1,
    mins: Int = 2,
    seconds: Int = 3,
    milliseconds: Int = 4
): LapTime = LapTime(
    hours = hours,
    mins = mins,
    seconds = seconds,
    millis = milliseconds
)