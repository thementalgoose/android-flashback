package tmg.flashback.statistics.network.models.overview

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import tmg.flashback.statistics.network.models.circuits.Circuit

typealias Overview = Map<String, OverviewRace>

@Keep
@Serializable
data class OverviewRace(
    val season: Int,
    val round: Int,
    val name: String,
    val circuit: Circuit,
    val date: String,
    val laps: String? = null,
    val time: String? = null,
    val hasQualifying: Boolean,
    val hasRace: Boolean,
    val schedule: List<Schedule>? = null,
) {
    companion object
}