package tmg.flashback.statistics.network.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.constructors.Constructor
import tmg.flashback.statistics.network.models.drivers.Driver
import tmg.flashback.statistics.network.models.overview.Schedule

@Keep
@Serializable
data class Round(
    val drivers: Map<String, Driver>,
    val constructors: Map<String, Constructor>,
    val data: RaceData,
    val race: Map<String, RaceResult>?,
    val sprint: Map<String, SprintResult>? = null,
    val qualifying: Map<String, QualifyingResult>?,
    val schedule: List<Schedule>? = null
) {
    companion object
}