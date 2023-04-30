package tmg.flashback.flashbackapi.api.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import tmg.flashback.flashbackapi.api.models.constructors.Constructor
import tmg.flashback.flashbackapi.api.models.drivers.Driver
import tmg.flashback.flashbackapi.api.models.overview.Schedule

@Keep
@Serializable
data class Round(
    val drivers: Map<String, Driver>,
    val constructors: Map<String, Constructor>,
    val data: RaceData,
    val race: Map<String, RaceResult>?,
    val sprintEvent: SprintEvent? = null,
    val qualifying: Map<String, QualifyingResult>?,
    val schedule: List<Schedule>? = null
) {
    companion object
}