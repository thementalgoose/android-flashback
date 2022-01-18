package tmg.flashback.statistics.network.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class SprintQualifyingResult(
    val points: Double,
    val gridPos: Int?,
    val finished: Int,
    val status: String,
    val time: String?
) {
    companion object
}