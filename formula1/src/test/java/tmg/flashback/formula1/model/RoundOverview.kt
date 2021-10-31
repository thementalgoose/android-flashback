package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

fun RoundOverview.Companion.model(
    date: LocalDate = LocalDate.of(1995, 10, 12),
    time: LocalTime? = LocalTime.of(12, 34, 0),
    season: Int = 2020,
    round: Int = 1,
    raceName: String = "name",
    circuitId: String = "circuitId",
    circuitName: String = "circuit",
    country: String = "country",
    countryISO: String = "countryISO",
    hasQualifying: Boolean = true,
    hasResults: Boolean = false
): RoundOverview = RoundOverview(
    date = date,
    time = time,
    season = season,
    round = round,
    raceName = raceName,
    circuitId = circuitId,
    circuitName = circuitName,
    country = country,
    countryISO = countryISO,
    hasQualifying = hasQualifying,
    hasResults = hasResults,
)