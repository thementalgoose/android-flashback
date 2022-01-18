package tmg.flashback.statistics.network.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.overview.Schedule

@Keep
@Serializable
data class Race(
    val data: RaceData,
    val race: Map<String, RaceResult>,
    val qualifying: Map<String, QualifyingResult>,
    val schedule: List<Schedule>? = null,
) {
    companion object
}