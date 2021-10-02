package tmg.flashback.firebase.models

data class FCircuit(
    val circuitName: String = "",
    val country: String = "",
    val countryISO: String? = "",
    val id: String = "",
    val locality: String = "",
    @Deprecated("This field shouldn't be used, use location object")
    val locationLat: Double? = null,
    @Deprecated("This field shouldn't be used, use location object")
    val locationLng: Double? = null,
    val location: FCircuitLocation? = null,
    val wikiUrl: String? = null,
    val results: Map<String, FCircuitResult>? = null
) {
    companion object
}

data class FCircuitLocation(
    val lat: String? = null,
    val lng: String? = null
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