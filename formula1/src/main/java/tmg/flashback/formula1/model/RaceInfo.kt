package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

data class RaceInfo(
    val season: Int,
    val round: Int,
    val date: LocalDate,
    val time: LocalTime?,
    val name: String,
    val laps: String?,
    val format: RaceFormat,
    val wikipediaUrl: String?,
    val youtube: String?,
    val circuit: Circuit,
) {
    companion object
}