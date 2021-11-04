package tmg.flashback.statistics.network.models.races

import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.constructors.Constructor
import tmg.flashback.statistics.network.models.drivers.Driver

@Serializable
data class Season(
    val season: Int,
    val driverStandings: Map<String, DriverStandings>?,
    val constructorStandings: Map<String, ConstructorStandings>?,
    val drivers: Map<String, Driver>,
    val constructors: Map<String, Constructor>,
    val races: Map<String, Race>?
) {
    companion object
}
