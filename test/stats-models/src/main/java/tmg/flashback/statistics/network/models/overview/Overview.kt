package tmg.flashback.statistics.network.models.overview

import tmg.flashback.statistics.network.models.circuits.Circuit
import tmg.flashback.statistics.network.models.circuits.model

fun OverviewRace.Companion.model(
    season: Int = 2020,
    round: Int = 1,
    name: String = "name",
    circuit: Circuit = Circuit.model(),
    date: String = "1995-10-12",
    time: String? = "12:34",
    hasQualifying: Boolean = true,
    hasRace: Boolean = false
): OverviewRace = OverviewRace(
    season = season,
    round = round,
    name = name,
    circuit = circuit,
    date = date,
    time = time,
    hasQualifying = hasQualifying,
    hasRace = hasRace
)