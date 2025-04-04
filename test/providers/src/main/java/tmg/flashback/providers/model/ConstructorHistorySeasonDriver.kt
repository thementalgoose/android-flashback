package tmg.flashback.providers.model

import tmg.flashback.formula1.model.ConstructorHistorySeasonDriver
import tmg.flashback.formula1.model.DriverEntry

fun ConstructorHistorySeasonDriver.Companion.model(
    driver: DriverEntry = DriverEntry.model(),
    points: Double = 1.0,
    wins: Int = 1,
    races: Int = 1,
    podiums: Int = 1,
    finishesInPoints: Int = 1,
    polePosition: Int = 1,
    championshipStanding: Int? = 1
): ConstructorHistorySeasonDriver = ConstructorHistorySeasonDriver(
    driver = driver,
    points = points,
    wins = wins,
    races = races,
    podiums = podiums,
    finishesInPoints = finishesInPoints,
    polePosition = polePosition,
    championshipStanding = championshipStanding
)