package tmg.flashback.providers.model

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.DriverHistorySeason
import tmg.flashback.formula1.model.DriverHistorySeasonRace

fun DriverHistorySeason.Companion.model(
    championshipStanding: Int? = 1,
    isInProgress: Boolean = true,
    points: Double = 1.0,
    season: Int = 2020,
    constructors: List<Constructor> = listOf(
        Constructor.model()
    ),
    raceOverview: List<DriverHistorySeasonRace> = listOf(
        DriverHistorySeasonRace.model()
    ),
): DriverHistorySeason = DriverHistorySeason(
    championshipStanding = championshipStanding,
    isInProgress = isInProgress,
    points = points,
    season = season,
    constructors = constructors,
    raceOverview = raceOverview
)