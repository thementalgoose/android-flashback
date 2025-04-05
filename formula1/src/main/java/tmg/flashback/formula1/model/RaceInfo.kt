package tmg.flashback.formula1.model

import java.time.LocalDate
import java.time.LocalTime

data class RaceInfo(
    val season: Int,
    val round: Int,
    val date: LocalDate,
    val time: LocalTime?,
    val name: String,
    val laps: String?,
    val youtube: String?,
    val wikipediaUrl: String?,
    val circuit: Circuit,
) {
    companion object
}