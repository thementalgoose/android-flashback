package tmg.flashback.statistics.network.models.constructors

import tmg.flashback.statistics.network.models.drivers.DriverData
import tmg.flashback.statistics.network.models.drivers.model

fun ConstructorStandingDriver.Companion.model(
    driver: DriverData = DriverData.model(),
    points: Double = 1.0,
    wins: Int? = 6,
    races: Int? = 5,
    podiums: Int? = 4,
    pointsFinishes: Int? = 3,
    pole: Int? = 2,
    championshipPosition: Int? = 1,
): ConstructorStandingDriver = ConstructorStandingDriver(
    driver = driver,
    points = points,
    wins = wins,
    races = races,
    podiums = podiums,
    pointsFinishes = pointsFinishes,
    pole = pole,
    championshipPosition = championshipPosition,
)