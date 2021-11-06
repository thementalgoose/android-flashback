package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

fun RaceInfo.Companion.model(
    season: Int = 2020,
    round: Int = 1,
    date: LocalDate = LocalDate.of(1995, 10, 12),
    time: LocalTime? = LocalTime.of(12, 34),
    name: String = "name",
    wikipediaUrl: String? = "wikiUrl",
    circuit: Circuit = Circuit.model(),
): RaceInfo = RaceInfo(
    season = season,
    round = round,
    date = date,
    time = time,
    name = name,
    wikipediaUrl = wikipediaUrl,
    circuit = circuit,
)