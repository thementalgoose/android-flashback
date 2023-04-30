package tmg.flashback.flashbackapi.api.models.races

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import tmg.flashback.flashbackapi.api.models.circuits.Circuit

@Keep
@Serializable
data class RaceData(
    val season: Int,
    val round: Int,
    val name: String,
    val date: String,
    val laps: String? = null,
    val time: String? = null,
    val circuit: Circuit,
    val wikiUrl: String? = null,
    val youtubeUrl: String? = null
) {
    companion object
}