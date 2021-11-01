package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate

data class Driver(
    val id: String,
    val firstName: String,
    val lastName: String,
    val code: String?,
    val number: Int?,
    val wikiUrl: String?,
    val photoUrl: String?,
    val dateOfBirth: LocalDate,
    val nationality: String,
    val nationalityISO: String,
)