package tmg.flashback.firebase.models

data class FCircuit(
    val circuitName: String = "",
    val country: String = "",
    val countryISO: String? = "",
    val id: String = "",
    val locality: String = "",
    val locationLat: Double? = null,
    val locationLng: Double? = null,
    val wikiUrl: String? = null,
    val results: Map<String, FCircuitResult>? = null
) {
    companion object
}

data class FCircuitResult(
    val date: String? = null,
    val time: String? = null,
    val name: String = "",
    val season: Int = -1,
    val round: Int = -1,
    val wikiUrl: String? = null
) {
    companion object
}