package tmg.flashback.statistics.network.models.constructors

import tmg.flashback.statistics.network.models.drivers.DriverData

data class Constructors(
    val construct: ConstructorData,
    val standings: Map<String, ConstructorStanding>
)

data class ConstructorStanding(
    val season: Int,
    val championshipPosition: Int?,
    val points: Double?,
    val drivers: Map<String, ConstructorStandingDriver>
)

data class ConstructorDriverStandings(
    val season: Int,
    val championshipPosition: Int?,
    val points: Double?,
    val drivers: Map<String, Double>
)

data class ConstructorStandingDriver(
    val driver: DriverData,
    val points: Double,
    val championshipPosition: Int,
    val wins: Int,
)