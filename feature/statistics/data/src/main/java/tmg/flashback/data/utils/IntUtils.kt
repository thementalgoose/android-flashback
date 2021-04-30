package tmg.flashback.data.utils

import kotlin.math.floor

fun Int?.toMaxIfZero(): Int = if (this == null || this == 0) Int.MAX_VALUE else this

fun Int.extendTo(toCharacters: Int = 2): String {
    var chars: String = ""
    for (i in this.toString().length until toCharacters) {
        chars += "0"
    }
    return "$chars$this"
}

fun Int.positive(): Int = when {
    this < 0 -> 0
    else -> this
}

val Int.hoursAndMins: Pair<Int, Int>
    get() {
        if (this < 0) {
            return Pair(0, 0)
        }
        val hours = floor(this / 3600f).toInt()
        val minutes = floor((this % 3600f) / 60f).toInt()
        return Pair(hours, minutes)
    }