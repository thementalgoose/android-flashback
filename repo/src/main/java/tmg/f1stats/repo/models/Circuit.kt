package tmg.f1stats.repo.models

data class Circuit(
    val circuitId: String,
    val wikiUrl: String,
    val circuitName: String,
    val locationLat: Double?,
    val locationLng: Double?,
    val locality: String,
    val country: String
)