package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

data class OverviewRace(
    val date: LocalDate,
    val time: LocalTime?,
    val season: Int,
    val round: Int,
    val raceName: String,
    val circuitId: String,
    val circuitName: String,
    val laps: String?,
    val country: String,
    val countryISO: String,
    val hasQualifying: Boolean,
    val hasResults: Boolean,
    val schedule: List<Schedule>
) {
    companion object
}