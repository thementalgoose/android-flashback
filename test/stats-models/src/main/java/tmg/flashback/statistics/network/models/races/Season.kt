package tmg.flashback.statistics.network.models.races

import tmg.flashback.statistics.network.models.constructors.Constructor
import tmg.flashback.statistics.network.models.constructors.model
import tmg.flashback.statistics.network.models.drivers.Driver
import tmg.flashback.statistics.network.models.drivers.model

fun Season.Companion.model(
    season: Int = 2020,
    driverStandings: Map<String, DriverStandings>? = mapOf(
        "driverId" to DriverStandings.model()
    ),
    constructorStandings: Map<String, ConstructorStandings>? = mapOf(
        "constructorId" to ConstructorStandings.model()
    ),
    drivers: Map<String, Driver> = mapOf(
        "driverId" to Driver.model()
    ),
    constructors: Map<String, Constructor> = mapOf(
        "constructorId" to Constructor.model()
    ),
    races: Map<String, Race>? = mapOf(
        "r1" to Race.model()
    )
): Season = Season(
    season = season,
    driverStandings = driverStandings,
    constructorStandings = constructorStandings,
    drivers = drivers,
    constructors = constructors,
    races = races
)