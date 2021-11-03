package tmg.flashback.formula1.model

import tmg.flashback.formula1.model.Location

data class Circuit(
    val id: String,
    val name: String,
    val wikiUrl: String?,
    val city: String,
    val country: String,
    val countryISO: String,
    val location: Location?
) {
    companion object
}