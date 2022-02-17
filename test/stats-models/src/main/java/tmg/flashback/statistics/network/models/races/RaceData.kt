package tmg.flashback.statistics.network.models.races

import tmg.flashback.statistics.network.models.circuits.Circuit
import tmg.flashback.statistics.network.models.circuits.model

fun RaceData.Companion.model(
    season: Int = 2020,
    round: Int = 1,
    name: String = "name",
    date: String = "2020-10-12",
    time: String? = "12:34",
    format: RaceFormat = RaceFormat.model(),
    laps: String? = "12",
    circuit: Circuit = Circuit.model(),
    wikiUrl: String? = "wikiUrl",
    youtubeUrl: String? = "youtube"
): RaceData = RaceData(
    season = season,
    round = round,
    name = name,
    date = date,
    time = time,
    laps = laps,
    format = format,
    circuit = circuit,
    wikiUrl = wikiUrl,
    youtubeUrl = youtubeUrl
)