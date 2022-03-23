package tmg.flashback.statistics.models

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.formula1.model.Schedule

fun WeekendOverview.Companion.model(
    season: Int = 2020,
    raceName: String = "name",
    circuitName: String = "circuitName",
    circuitId: String = "circuitId",
    raceCountry: String = "country",
    raceCountryISO: String = "countryISO",
    date: LocalDate = LocalDate.of(2020, 10, 12),
    round: Int = 1,
    hasQualifying: Boolean = true,
    hasResults: Boolean = false,
    schedule: List<Schedule> = listOf(
        Schedule(
            label = "label",
            date = LocalDate.of(2020, 1, 1),
            time = LocalTime.of(12, 34)
        )
    )
): WeekendOverview = WeekendOverview(
    season = season,
    raceName = raceName,
    circuitName = circuitName,
    circuitId = circuitId,
    raceCountry = raceCountry,
    raceCountryISO = raceCountryISO,
    date = date,
    round = round,
    hasQualifying = hasQualifying,
    hasResults = hasResults,
    schedule = schedule,
)