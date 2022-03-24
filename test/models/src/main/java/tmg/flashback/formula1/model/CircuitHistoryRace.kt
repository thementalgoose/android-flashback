package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

fun CircuitHistoryRace.Companion.model(
    name: String = "name",
    season: Int = 2020,
    round: Int = 1,
    wikiUrl: String? = "wikiUrl",
    date: LocalDate = LocalDate.of(1995, 10, 12),
    time: LocalTime? = LocalTime.of(12, 34)
): CircuitHistoryRace = CircuitHistoryRace(
    name = name,
    season = season,
    round = round,
    wikiUrl = wikiUrl,
    date = date,
    time = time,
)