package tmg.flashback.stats.ui.search

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

sealed class SearchItem(
    val id: String,
    val searchBy: String?,
) {
    data class Driver(
        val driverId: String,
        val name: String,
        val nationality: String,
        val nationalityISO: String,
        val imageUrl: String?
    ): SearchItem(
        id = "driver-$driverId",
        searchBy = "${name.lowercase()} ${nationality.lowercase()}"
    ) {
        companion object
    }

    data class Constructor(
        val constructorId: String,
        val name: String,
        val nationality: String,
        val nationalityISO: String,
        val colour: Int
    ): SearchItem(
        id = "constructor-$constructorId",
        searchBy = "${name.lowercase()} ${nationality.lowercase()}"
    ) {
        companion object
    }

    data class Circuit(
        val circuitId: String,
        val name: String,
        val nationality: String,
        val nationalityISO: String,
        val location: String
    ): SearchItem(
        id = "circuit-$circuitId",
        searchBy = "${name.lowercase()} ${nationality.lowercase()} ${location.lowercase()}"
    ) {
        companion object
    }

    data class Race(
        val raceId: String,
        val season: Int,
        val round: Int,
        val raceName: String,
        val country: String,
        val countryISO: String,
        val circuitId: String,
        val circuitName: String,
        val date: LocalDate
    ): SearchItem(
        id = "race-${raceId}",
        searchBy = "$season $round $season ${raceName.lowercase()} ${circuitName.lowercase()} ${country.lowercase()} ${DateTimeFormatter.ofPattern("MMMM").format(date).lowercase()}"
    ) {
        companion object
    }

    object Placeholder: SearchItem(
        id = "placeholder",
        searchBy = null
    )

    object Advert: SearchItem(
        id = "advert",
        searchBy = null
    )

    object ErrorItem: SearchItem(
        id = "error",
        searchBy = null
    )

    companion object
}