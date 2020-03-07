package tmg.f1stats.repo.models

import kotlin.IllegalArgumentException

// TODO: Clean up the internals of this class!
data class LapTime(
    val mins: Int,
    val seconds: Int,
    val millis: Int
) {
    constructor(time: String): this(
        mins = time.split(":")[0].toIntOrNull() ?: 0,
        seconds = time.split(":")[1].split(".")[0].toIntOrNull() ?: 0,
        millis = time.split(".")[1].toIntOrNull() ?: 0
    )

    init {
        if (!checkLapTime(time)) {
            println("Lap time $time is considered invalid!")
            throw IllegalArgumentException("Lap time $time is considered invalid!")
        }
    }

    val time: String
        get() = "$mins:${seconds.extendTo(2)}.${millis.extendTo(3)}"

    private fun Int.extendTo(characters: Int = 2): String {
        var returnString: String = "";
        for (x in this.toString().length until characters) {
            returnString += "0"
        }
        return returnString + this
    }

    companion object {
        fun checkLapTime(time: String): Boolean {
            return !(!time.contains(":") || time.split(":").size != 2 ||
                    !time.contains(".") || time.split(".").size != 2)
        }
    }
}