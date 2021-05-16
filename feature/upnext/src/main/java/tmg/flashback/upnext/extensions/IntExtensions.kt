package tmg.flashback.upnext.extensions

import kotlin.math.floor

/**
 * Given that the number is in seconds, convert it to a pair containing hours and minutes
 */
val Int.secondsToHHmm: Pair<Int, Int>
    get() {
        if (this < 0) {
            return Pair(0, 0)
        }
        val hours = floor(this / 3600f).toInt()
        val minutes = floor((this % 3600f) / 60f).toInt()
        return Pair(hours, minutes)
    }