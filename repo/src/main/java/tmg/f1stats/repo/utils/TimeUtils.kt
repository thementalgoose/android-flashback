package tmg.f1stats.repo.utils

import org.threeten.bp.LocalTime
import tmg.f1stats.repo.enums.LapTimeFormats
import tmg.f1stats.repo.models.LapTime

fun LapTime.addDelta(hours: Int = 0, mins: Int = 0, seconds: Int = 0, millis: Int = 0): LapTime {
    val lapMillis: Int = LapTime(hours, mins, seconds, millis).totalMillis
    return this.add(lapMillis)
}

fun LapTime.add(millis: Int): LapTime {
    return LapTime(this.totalMillis + millis)
}

fun LapTime.addDelta(time: String?): LapTime {
    if (time == null) {
        return this
    }
    return addDelta(
        mins = time.split(":")[0].toIntOrNull() ?: 0,
        seconds = time.split(":")[1].split(".")[0].toIntOrNull() ?: 0,
        millis = time.split(".")[1].toIntOrNull() ?: 0
    )
}

fun String.toLocalTime(): LocalTime {
    val time = if (this.contains("+")) {
        this.replace("+", "")
    }
    else {
        this
    }
    if (time.matches(LapTimeFormats.SECOND_MILLIS.regex)) {
        val seconds = time.split(".")[0].toIntOrNull() ?: 0
        val millis = time.split(".")[1].toIntOrNull() ?: 0
        return LocalTime.of(0, 0, seconds, millis * 1_000_000)
    }
    if (time.matches(LapTimeFormats.MIN_SECOND_MILLIS.regex)) {
        val mins = time.split(":")[0].toIntOrNull() ?: 0
        val seconds = time.split(":")[1].split(".")[0].toIntOrNull() ?: 0
        val millis = time.split(".")[1].toIntOrNull() ?: 0
        return LocalTime.of(0, mins, seconds, millis * 1_000_000)
    }
    if (time.matches(LapTimeFormats.HOUR_MIN_SECOND_MILLIS.regex)) {
        val hours = time.split(":")[0].toIntOrNull() ?: 0
        val mins = time.split(":")[1].toIntOrNull() ?: 0
        val seconds = time.split(":")[2].split(".")[0].toIntOrNull() ?: 0
        val millis = time.split(".")[1].toIntOrNull() ?: 0
        return LocalTime.of(hours, mins, seconds, millis * 1_000_000)
    }
    return LocalTime.of(0, 0, 0, 0)
}

fun String.toLapTime(): LapTime {
    val localTime = this.toLocalTime()
    return LapTime(
        hours = localTime.hour,
        mins = localTime.minute,
        seconds = localTime.second,
        millis = localTime.nano / 1_000_000
    )
}