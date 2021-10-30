package tmg.flashback.statistics.network.models.races

import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.constructors.ConstructorData
import tmg.flashback.statistics.network.models.constructors.ConstructorDriverStandings
import tmg.flashback.statistics.network.models.drivers.DriverConstructorStandings
import tmg.flashback.statistics.network.models.drivers.DriverData
import tmg.flashback.statistics.network.models.drivers.DriverStanding

@Serializable
data class Season(
    val season: Int,
    val driverStandings: Map<String, DriverConstructorStandings>,
    val constructorStandings: Map<String, ConstructorDriverStandings>,
    val drivers: Map<String, DriverData>,
    val constructors: Map<String, ConstructorData>,
    val races: Map<String, Race>
)