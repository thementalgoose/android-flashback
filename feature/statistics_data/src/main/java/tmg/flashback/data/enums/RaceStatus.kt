package tmg.flashback.data.enums

typealias RaceStatus = String

const val raceStatusUnknown: String = "Unknown"

fun String.isStatusFinished(): Boolean {
    return raceStatusFinished.contains(this)
}

val raceStatusFinished: List<String> = listOf(
    "Finished",
    "+1 Lap",
    "+2 Laps",
    "+3 Laps",
    "+4 Laps",
    "+5 Laps",
    "+6 Laps",
    "+7 Laps",
    "+8 Laps",
    "+9 Laps"
)