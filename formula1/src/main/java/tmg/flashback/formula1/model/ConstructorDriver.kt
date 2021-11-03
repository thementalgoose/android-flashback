package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate

@Deprecated("Should not be used anymore", replaceWith = ReplaceWith("DriverConstructor"))
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

    fun getDriver(): Driver {
        return Driver(
            id = id,
            firstName = firstName,
            lastName = lastName,
            code = code,
            number = number,
            wikiUrl = wikiUrl,
            photoUrl = photoUrl,
            dateOfBirth = dateOfBirth,
            nationality = nationality,
            nationalityISO = nationalityISO
        )
    }
}