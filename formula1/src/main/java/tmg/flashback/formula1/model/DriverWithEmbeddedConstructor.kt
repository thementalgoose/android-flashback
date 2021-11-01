package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate

/**
 * Data class to hold driver variables
 */
data class DriverWithEmbeddedConstructor(
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
    val constructors: Map<Int, Constructor>,
    val startingConstructor: Constructor

) {
    val constructorAtEndOfSeason: Constructor?
        get() = constructors
            .maxByOrNull { it.key }
            ?.value

    fun toConstructorDriver(round: Int = 0): ConstructorDriver {
        return ConstructorDriver(
            id = this.id,
            firstName = this.firstName,
            lastName = this.lastName,
            code = this.code,
            number = this.number ?: 0,
            wikiUrl = this.wikiUrl ?: "",
            photoUrl = this.photoUrl,
            dateOfBirth = this.dateOfBirth,
            nationality = this.nationality,
            nationalityISO = this.nationalityISO,
            constructor = this.constructors.getOrElse(round, { this.startingConstructor }),
        )
    }

    val name: String
        get() = "$firstName $lastName"
}