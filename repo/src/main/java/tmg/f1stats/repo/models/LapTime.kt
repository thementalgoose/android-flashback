package tmg.f1stats.repo.models

import org.threeten.bp.LocalTime
import tmg.f1stats.repo.utils.toLocalTime
import kotlin.math.floor

// TODO: Clean up the internals of this class!
data class LapTime(
    val hours: Int,
    val mins: Int,
    val seconds: Int,
    val millis: Int
) {

    constructor(millis: Int): this(
        LocalTime.ofNanoOfDay(millis * 1_000_000L).hour,
        LocalTime.ofNanoOfDay(millis * 1_000_000L).minute,
        LocalTime.ofNanoOfDay(millis * 1_000_000L).second,
        LocalTime.ofNanoOfDay(millis * 1_000_000L).nano / 1_000_000
    )

    val totalMillis: Int
        get() = (hours * 1000 * 60 * 60) +
                (mins * 1000 * 60) +
                (seconds * 1000) +
                millis

    val time: String
        get() = if (hours == 0) {
            "${hours}:${mins}:${seconds.extendTo(2)}.${millis.extendTo(3)}"
        } else {
            "${mins}:${seconds.extendTo(2)}.${millis.extendTo(3)}"
        }
}

private fun Int.extendTo(toCharacters: Int = 2): String {
    var chars: String = ""
    for (i in this.toString().length..toCharacters) {
        chars += "0"
    }
    return "$chars$this"
}
