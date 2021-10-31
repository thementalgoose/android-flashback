package tmg.flashback.statistics.network.models.overview

import kotlinx.serialization.Serializable

typealias Overview = Map<String, OverviewRace>

@Serializable
data class OverviewRace(
    val season: Int,
    val round: Int,
    val name: String,
    val circuit: String,
    val circuitId: String,
    val country: String,
    val countryISO: String,
    val date: String,
    val time: String? = null,
    val hasQualifying: Boolean,
    val hasRace: Boolean
) {
    companion object
}