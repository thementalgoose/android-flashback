package tmg.flashback.statistics.network.models.races

import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.constructors.ConstructorData
import tmg.flashback.statistics.network.models.drivers.DriverData

@Serializable
data class Season(
    val season: Int,
    val driverStandings: Map<String, DriverStandings>?,
    val constructorStandings: Map<String, ConstructorStandings>?,
    val drivers: Map<String, DriverData>,
    val constructors: Map<String, ConstructorData>,
    val races: Map<String, Race>?
)

@Serializable
data class DriverStandings(
    val driverId: String,
    val points: Double,
    val inProgress: Boolean? = null,
    val races: Int? = null, // TODO: Remove nullability on here!
    val position: Int? = null,
    val constructors: Map<String, Double>
)

@Serializable
data class ConstructorStandings(
    val constructorId: String,
    val points: Double,
    val inProgress: Boolean? = null,
    val races: Int? = null, // TODO: Remove nullability on here!
    val position: Int? = null,
    val drivers: Map<String, Double>
)