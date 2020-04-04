package tmg.f1stats.repo.models

import org.threeten.bp.LocalDate

/**
 * Data class to hold driver variables
 */
open class Driver(
    val id: String,
    val firstName: String,
    val lastName: String,
    val code: String?,
    val number: Int,
    val wikiUrl: String,
    val photoUrl: String?,
    val dateOfBirth: LocalDate,
    val nationality: String,
    val nationalityISO: String
) {
    fun forRound(constructor: Constructor): RoundDriver {
        return RoundDriver(
            id = id,
            firstName = firstName,
            lastName = lastName,
            code = code,
            number = number,
            wikiUrl = wikiUrl,
            photoUrl = photoUrl,
            dateOfBirth = dateOfBirth,
            nationality = nationality,
            nationalityISO = nationalityISO,
            constructor = constructor
        )
    }

    val name: String
        get() = "$firstName $lastName"
}

/**
 * Class to hold the driver in a specific round of a season
 * - Includes what constructor they are driving for in this round
 */
class RoundDriver(
    id: String,
    firstName: String,
    lastName: String,
    code: String?,
    number: Int,
    wikiUrl: String,
    photoUrl: String?,
    dateOfBirth: LocalDate,
    nationality: String,
    nationalityISO: String,
    val constructor: Constructor
): Driver(
    id,
    firstName,
    lastName,
    code,
    number,
    wikiUrl,
    photoUrl,
    dateOfBirth,
    nationality,
    nationalityISO
)