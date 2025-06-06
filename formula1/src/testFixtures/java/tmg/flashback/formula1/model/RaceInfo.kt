package tmg.flashback.formula1.model

import java.time.LocalDate
import java.time.LocalTime

fun RaceInfo.Companion.model(
    season: Int = 2020,
    round: Int = 1,
    date: LocalDate = LocalDate.of(2020, 10, 12),
    time: LocalTime? = LocalTime.of(12, 34),
    name: String = "name",
    laps: String? = "12",
    wikipediaUrl: String? = "wikiUrl",
    youtube: String? = "youtube",
    circuit: Circuit = Circuit.model(),
): RaceInfo = RaceInfo(
    season = season,
    round = round,
    date = date,
    time = time,
    laps = laps,
    name = name,
    wikipediaUrl = wikipediaUrl,
    youtube = youtube,
    circuit = circuit,
)