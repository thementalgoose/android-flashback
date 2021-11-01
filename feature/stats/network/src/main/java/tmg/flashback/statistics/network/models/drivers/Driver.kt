package tmg.flashback.statistics.network.models.drivers

import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.constructors.ConstructorData
import tmg.flashback.statistics.network.models.races.RaceData

@Serializable
data class Driver(
    val driver: DriverData,
    val standings: Map<String, DriverStanding>
)

@Serializable
data class DriverStanding(
    val season: Int,
    val championshipPosition: Int?,
    val points: Double,
    val inProgress: Boolean,
    val races: Map<String, DriverStandingRace>
)

@Serializable
data class DriverConstructorStandings(
    val season: Int,
    val championshipPosition: Int?,
    val races: Map<String, DriverStandingRace>,
    val constructors: Map<String, Double>
)

@Serializable
data class DriverStandingRace(
    val construct: ConstructorData,
    val race: RaceData,
    val sprintQuali: Boolean?,
    val qualified: Int?,
    val gridPos: Int?,
    val finished: Int,
    val status: String,
    val points: Double
)