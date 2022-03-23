package tmg.flashback.statistics.models

import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Schedule

data class WeekendOverview(
    val season: Int,
    val raceName: String,
    val circuitName: String,
    val circuitId: String,
    val raceCountry: String,
    val raceCountryISO: String,
    val date: LocalDate,
    val round: Int,
    val hasQualifying: Boolean,
    val hasResults: Boolean,
    val schedule: List<Schedule>
) {
    companion object

    val key: String by lazy {
        "s${season}r${round}"
    }
}