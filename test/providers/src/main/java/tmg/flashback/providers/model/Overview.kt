package tmg.flashback.providers.model

import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace

fun Overview.Companion.model(
    season: Int = 2020,
    overviewRaces: List<OverviewRace> = listOf(OverviewRace.model())
): Overview = Overview(
    season = season,
    overviewRaces = overviewRaces
)