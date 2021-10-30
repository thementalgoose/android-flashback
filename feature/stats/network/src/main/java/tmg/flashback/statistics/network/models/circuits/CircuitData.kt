package tmg.flashback.statistics.network.models.circuits

data class CircuitData(
    val id: String,
    val name: String,
    val wikiUrl: String?,
    val location: Location?,
    val city: String,
    val country: String,
    val countryISO: String
)