package tmg.flashback.flashbackapi.api.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import tmg.flashback.flashbackapi.api.models.overview.Schedule

@Keep
@Serializable
data class Race(
    val data: RaceData,
    val race: Map<String, RaceResult>? = null,
    val sprintEvent: SprintEvent? = null,
    val qualifying: Map<String, QualifyingResult>? = null,
    val schedule: List<Schedule>? = null,
) {
    companion object
}