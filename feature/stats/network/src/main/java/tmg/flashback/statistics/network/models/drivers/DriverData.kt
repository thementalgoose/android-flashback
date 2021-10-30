package tmg.flashback.statistics.network.models.drivers

data class DriverData(
    val id: String,
    val firstName: String,
    val lastName: String,
    val dob: String,
    val nationality: String,
    val nationalityISO: String,
    val photoUrl: String?,
    val wikiUrl: String?,
    val code: String?,
    val permanentNumber: String?
)