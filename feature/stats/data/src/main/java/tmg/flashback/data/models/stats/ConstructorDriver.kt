package tmg.flashback.data.models.stats

import org.threeten.bp.LocalDate

data class ConstructorDriver(
    val id: String,
    val firstName: String,
    val lastName: String,
    val code: String?,
    val number: Int,
    val wikiUrl: String,
    val photoUrl: String?,
    val dateOfBirth: LocalDate,
    val nationality: String,
    val nationalityISO: String,
    val constructor: Constructor
) {
    val name: String
        get() = "$firstName $lastName"
}