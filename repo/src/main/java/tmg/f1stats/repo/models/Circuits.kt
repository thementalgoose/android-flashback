package tmg.f1stats.repo.models

data class Circuits(
    val circuitId: String,
    val wikiUrl: String,
    val photoUrl: String,
    val locationLat: Double,
    val locationLng: Double,
    val locality: String,
    val country: String
)