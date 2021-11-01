package tmg.flashback.statistics.network.models.race

import tmg.flashback.statistics.network.models.circuits.CircuitData
import tmg.flashback.statistics.network.models.circuits.model
import tmg.flashback.statistics.network.models.races.RaceData

fun RaceData.Companion.model(
    season: Int = 2020,
    round: Int = 1,
    name: String = "name",
    date: String = "1995-10-12",
    time: String? = "12:34",
    circuit: CircuitData = CircuitData.model(),
    wikiUrl: String? = "wikiUrl",
): RaceData = RaceData(
    season = season,
    round = round,
    name = name,
    date = date,
    time = time,
    circuit = circuit,
    wikiUrl = wikiUrl,
)