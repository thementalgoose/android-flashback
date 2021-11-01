package tmg.flashback.statistics.network.models.drivers

import kotlinx.serialization.Serializable

typealias AllDrivers = Map<String, DriverData>

@Serializable
data class DriverData(
    val id: String,
    val firstName: String,
    val lastName: String,
    val dob: String,
    val nationality: String,
    val nationalityISO: String,
    val photoUrl: String? = null,
    val wikiUrl: String? = null,
    val code: String? = null,
    val permanentNumber: String? = null
)