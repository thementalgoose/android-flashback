package tmg.f1stats.repo.models

class Circuit(
    val id: String,
    val name: String,
    val wikiUrl: String,
    val locality: String,
    val country: String,
    val countryISO: String,
    val locationLat: Double,
    val locationLng: Double
)