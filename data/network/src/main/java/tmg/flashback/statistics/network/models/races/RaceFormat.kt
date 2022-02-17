package tmg.flashback.statistics.network.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class RaceFormat(
    val qualifying: String? = null,
    val sprint: String? = null,
    val race: String? = null
) {
    companion object
}