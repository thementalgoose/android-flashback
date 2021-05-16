package tmg.flashback.data.models.stats

import org.threeten.bp.LocalDate

/**
 * Data class to hold driver variables
 */
data class Driver(
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
        val constructorAtEndOfSeason: Constructor
) {
    fun forRound(constructor: Constructor = constructorAtEndOfSeason): RoundDriver {
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
                constructor = constructor,
                constructorAtEndOfSeason = constructorAtEndOfSeason
        )
    }

    val name: String
        get() = "$firstName $lastName"
}