package tmg.flashback.data.models.stats

import org.threeten.bp.LocalDate
import tmg.flashback.data.models.ConstructorDriver

/**
 * Class to hold the driver in a specific round of a season
 * - Includes what constructor they are driving for in this round
 */
// TODO Convert this to just one driver object with map of constructors
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
    fun toConstructorDriver(): ConstructorDriver {
        return ConstructorDriver(
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
                constructor = constructorAtEndOfSeason
        )
    }

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