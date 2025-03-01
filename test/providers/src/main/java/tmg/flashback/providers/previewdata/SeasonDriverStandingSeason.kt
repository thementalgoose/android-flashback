package tmg.flashback.providers.previewdata

import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandingSeasonConstructor

internal fun SeasonDriverStandingSeason.Companion.model(
    season: Int = 2020,
    driver: Driver = Driver.model(),
    points: Double = 1.0,
    inProgress: Boolean = true,
    inProgressName: String? = "name",
    inProgressRound: Int? = 1,
    races: Int = 1,
    championshipPosition: Int? = 1,
    constructors: List<SeasonDriverStandingSeasonConstructor> = listOf(
        SeasonDriverStandingSeasonConstructor.model()
    ),
): SeasonDriverStandingSeason = SeasonDriverStandingSeason(
    season = season,
    driver = driver,
    points = points,
    inProgress = inProgress,
    inProgressName = inProgressName,
    inProgressRound = inProgressRound,
    races = races,
    championshipPosition = championshipPosition,
    constructors = constructors
)