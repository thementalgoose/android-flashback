package tmg.flashback.flashbackapi.api.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import tmg.flashback.flashbackapi.api.models.constructors.Constructor
import tmg.flashback.flashbackapi.api.models.drivers.Driver
import tmg.flashback.flashbackapi.api.models.overview.Event

@Keep
@Serializable
data class Season(
    val season: Int,
    val driverStandings: Map<String, DriverStandings>?,
    val constructorStandings: Map<String, ConstructorStandings>?,
    val drivers: Map<String, Driver>,
    val constructors: Map<String, Constructor>,
    val races: Map<String, Race>?,
    val events: List<Event>?
) {
    companion object
}
