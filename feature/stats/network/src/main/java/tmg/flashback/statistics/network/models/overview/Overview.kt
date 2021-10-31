package tmg.flashback.statistics.network.models.overview

import kotlinx.serialization.Serializable

@Serializable
class Overview: HashMap<String, OverviewRace>()

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
    val time: String?,
    val hasQualifying: Boolean,
    val hasResults: Boolean
)