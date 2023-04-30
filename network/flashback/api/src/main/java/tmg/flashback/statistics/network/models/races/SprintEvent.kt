package tmg.flashback.statistics.network.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class SprintEvent(
    val qualifying: Map<String, SprintQualifyingResult>?,
    val race: Map<String, SprintRaceResult>?,
)