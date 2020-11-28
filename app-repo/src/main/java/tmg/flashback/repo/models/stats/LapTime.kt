package tmg.flashback.repo.models.stats

import org.threeten.bp.LocalTime
import tmg.flashback.repo.utils.extendTo
import kotlin.math.abs

// TODO: Clean up the internals of this class!
data class LapTime(
    val hours: Int = 0,
    val mins: Int = 0,
    val seconds: Int = 0,
    val millis: Int = 0
) {

    constructor(): this(
        -1,
        -1,
        -1,
        -1
    )

    constructor(millis: Int): this(
        LocalTime.ofNanoOfDay(millis * 1_000_000L).hour,
        LocalTime.ofNanoOfDay(millis * 1_000_000L).minute,
        LocalTime.ofNanoOfDay(millis * 1_000_000L).second,
        LocalTime.ofNanoOfDay(millis * 1_000_000L).nano / 1_000_000
    )

    val noTime: Boolean
        get() = hours == -1 && mins == -1 && seconds == -1 && millis == -1

    val totalMillis: Int
        get() = if (noTime) 0 else (hours * 1000 * 60 * 60) +
                (mins * 1000 * 60) +
                (seconds * 1000) +
                millis

    val time: String
        get() = when {
            noTime -> {
                "No time"
            }
            hours != 0 -> {
                "${hours}:${mins.extendTo(2)}:${seconds.extendTo(2)}.${millis.extendTo(3)}"
            }
            mins != 0 -> {
                "${mins}:${seconds.extendTo(2)}.${millis.extendTo(3)}"
            }
            else -> {
                "${seconds}.${millis.extendTo(3)}"
            }
        }

    fun deltaTo(lapTime: LapTime?): String? {
        if (lapTime == null) {
            return null
        }
        val diff = lapTime.totalMillis - this.totalMillis
        if (diff == 0) {
            return null
        }
        val newTime = LapTime(abs(diff))
        return "${if (diff < 0) "-" else "+"}$newTime"
    }

    override fun equals(other: Any?): Boolean {
        return this.time == (other as? LapTime)?.time
    }

    override fun hashCode(): Int {
        return this.time.hashCode()
    }

    override fun toString(): String {
        return time
    }
}

val noTime: LapTime = LapTime()
