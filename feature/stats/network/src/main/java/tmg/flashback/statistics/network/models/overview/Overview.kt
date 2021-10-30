package tmg.flashback.statistics.network.models.overview

typealias Overview = Map<String, OverviewRace>

data class OverviewRace(
    val season: Int,
    val round: Int,
    val name: String,
    val circuit: String,
    val circuitId: String,
    val country: String,
    val countryISO: String,
    val hasQualifying: Boolean,
    val hasResults: Boolean
)