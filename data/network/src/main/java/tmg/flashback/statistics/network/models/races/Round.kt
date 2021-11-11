package tmg.flashback.statistics.network.models.races

import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.constructors.Constructor
import tmg.flashback.statistics.network.models.drivers.Driver
import tmg.flashback.statistics.network.models.overview.Schedule

@Serializable
data class Round(
    val drivers: Map<String, Driver>,
    val constructors: Map<String, Constructor>,
    val data: RaceData,
    val race: Map<String, RaceResult>?,
    val qualifying: Map<String, QualifyingResult>?,
    val schedule: List<Schedule>?
) {
    companion object
}