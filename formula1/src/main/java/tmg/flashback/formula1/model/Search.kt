package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate

@Deprecated("Should not be used anymore", replaceWith = ReplaceWith("Driver"))
data class SearchDriver(
    val id: String,
    val firstName: String,
    val lastName: String,
    val image: String?,
    val nationality: String,
    val nationalityISO: String,
    val dateOfBirth: LocalDate,
    val wikiUrl: String?
)

@Deprecated("Should not be used anymore", replaceWith = ReplaceWith("Constructor"))
data class SearchConstructor(
    val id: String,
    val name: String,
    val nationality: String,
    val nationalityISO: String,
    val wikiUrl: String?,
    val colour: Int
)

@Deprecated("Should not be used anymore", replaceWith = ReplaceWith("Circuit"))
data class SearchCircuit(
    val id: String,
    val country: String,
    val countryISO: String,
    val location: Location?,
    val locationName: String,
    val name: String,
    val wikiUrl: String?
)