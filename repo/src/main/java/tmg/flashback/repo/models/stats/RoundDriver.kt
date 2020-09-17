package tmg.flashback.repo.models.stats

import org.threeten.bp.LocalDate

/**
 * Class to hold the driver in a specific round of a season
 * - Includes what constructor they are driving for in this round
 */
data class RoundDriver(
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
        val constructor: Constructor,
        val constructorAtEndOfSeason: Constructor
) {
    fun toDriver(): Driver {
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
                nationalityISO = nationalityISO,
                constructorAtEndOfSeason = constructorAtEndOfSeason
        )
    }

    val name: String
        get() = "$firstName $lastName"
}