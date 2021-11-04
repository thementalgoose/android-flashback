package tmg.flashback.formula1.model

fun LapTime.Companion.model(
    hours: Int = 0,
    mins: Int = 1,
    seconds: Int = 2,
    milliseconds: Int = 3
): LapTime = LapTime(
    hours = hours,
    mins = mins,
    seconds = seconds,
    millis = milliseconds
)