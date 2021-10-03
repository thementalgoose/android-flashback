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

data class FSearchConstructor(
    val constructors: Map<String, FSearchConstructorModel?>? = null
) {
    companion object
}

data class FSearchConstructorModel(
    val name: String? = null,
    val nat: String? = null,
    val natISO: String? = null,
    val wikiUrl: String? = null,
    val color: String? = null
) {
    companion object
}

data class FSearchCircuit(
    val circuits: Map<String, FSearchCircuitModel?>? = null
) {
    companion object
}

data class FSearchCircuitModel(
    val country: String? = null,
    val countryISO: String? = null,
    val location: FCircuitLocation? = FCircuitLocation(),
    val loc: String? = null,
    val name: String? = null,
    val wikiUrl: String? = null
) {
    companion object
}