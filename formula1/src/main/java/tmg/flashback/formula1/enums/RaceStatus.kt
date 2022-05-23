package tmg.flashback.formula1.enums

typealias RaceStatus = String

const val raceStatusUnknown: String = "Unknown"
const val raceStatusFinish: String = "Finished"

fun String.isStatusFinished(): Boolean {
    return raceStatusFinished.map { it.lowercase() }.contains(this.lowercase())
}

val raceStatusFinished: List<String> = listOf(
    "Finished",
    "Finish",
    "+1 Lap",
    "+1 Laps",
    "+2 Laps",
    "+3 Laps",
    "+4 Laps",
    "+5 Laps",
    "+6 Laps",
    "+7 Laps",
    "+8 Laps",
    "+9 Laps"
)