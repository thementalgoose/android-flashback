package tmg.flashback.firebase.models

data class FSearchDriver(
    val drivers: Map<String, FSearchDriverModel?>? = null
) {
    companion object
}

data class FSearchDriverModel(
    val dob: String? = null,
    val fname: String? = null,
    val image: String? = null,
    val nat: String? = null,
    val natISO: String? = null,
    val sname: String? = null,
    val wikiUrl: String? = null
) {
    companion object
}