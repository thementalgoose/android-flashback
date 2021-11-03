package tmg.flashback.statistics.network.models.constructors

import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.drivers.DriverData

@Serializable
data class Constructor(
    val construct: ConstructorData,
    val standings: Map<String, ConstructorStanding>
)

@Serializable
data class ConstructorStanding(
    val season: Int,
    val championshipPosition: Int?,
    val points: Double?,
    val inProgress: Boolean,
    val races: Int?,
    val drivers: Map<String, ConstructorStandingDriver>
)

@Serializable
data class ConstructorStandingDriver(
    val driver: DriverData,
    val points: Double,
    val wins: Int?,
    val races: Int?,
    val podiums: Int?,
    val pointsFinishes: Int?,
    val pole: Int?,
    val championshipPosition: Int?
)