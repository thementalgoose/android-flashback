package tmg.flashback.statistics.network.models.drivers

import tmg.flashback.statistics.network.models.constructors.ConstructorData
import tmg.flashback.statistics.network.models.races.RaceData

data class Drivers(
    val driver: DriverData,
    val standings: Map<String, DriverStanding>
)

data class DriverStanding(
    val season: Int,
    val championshipPosition: Int,
    val races: Map<String, DriverStandingRace>
)

data class DriverConstructorStandings(
    val season: Int,
    val championshipPosition: Int,
    val races: Map<String, DriverStandingRace>,
    val constructors: Map<String, Double>
)

data class DriverStandingRace(
    val construct: ConstructorData,
    val race: RaceData,
    val sprintQuali: Boolean?,
    val qualified: Int,
    val gridPos: Int?,
    val finished: Int,
    val status: String,
    val points: Double
)