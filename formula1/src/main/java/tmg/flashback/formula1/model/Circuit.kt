package tmg.flashback.formula1.model

import org.threeten.bp.LocalTime
import org.threeten.bp.LocalDate

data class Circuit(
    val id: String,
    val name: String,
    val wikiUrl: String?,
    val locality: String,
    val country: String,
    val countryISO: String,
    val location: Location?,
    val results: List<CircuitRace>
)

data class CircuitRace(
    val name: String,
    val season: Int,
    val round: Int,
    val wikiUrl: String,
    val date: LocalDate,
    val time: LocalTime?
)

data class CircuitSummary(
    val id: String,
    val name: String,
    val wikiUrl: String,
    val locality: String,
    val country: String,
    val countryISO: String,
    val location: Location?
)