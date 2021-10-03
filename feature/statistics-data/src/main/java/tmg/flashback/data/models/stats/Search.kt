package tmg.flashback.data.models.stats

import org.threeten.bp.LocalDate

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

data class SearchConstructor(
    val id: String,
    val name: String,
    val nationality: String,
    val nationalityISO: String,
    val wikiUrl: String,
    val colour: Int
)

data class SearchCircuit(
    val id: String,
    val country: String,
    val countryISO: String,
    val locationLat: Double,
    val locationLng: Double,
    val location: String,
    val name: String,
    val wikiUrl: String
)

data class SearchRace(
    val round: Int,
    val season: Int,
    val circuit: String,
    val circuitId: String,
    val country: String,
    val countryISO: String,
    val date: LocalDate,
    val name: String
)