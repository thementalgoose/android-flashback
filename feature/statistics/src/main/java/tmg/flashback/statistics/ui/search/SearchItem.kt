package tmg.flashback.statistics.ui.search

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem

sealed class SearchItem(
    @LayoutRes
    val layoutId: Int,
    val searchBy: String?,
) {
    data class Driver(
        val driverId: String,
        val name: String,
        val nationality: String,
        val nationalityISO: String,
        val imageUrl: String?
    ): SearchItem(
        layoutId = R.layout.view_search_driver,
        searchBy = name.lowercase()
    )

    data class Constructor(
        val constructorId: String,
        val name: String,
        val nationality: String,
        val nationalityISO: String,
        val colour: Int
    ): SearchItem(
        layoutId = R.layout.view_search_constructor,
        searchBy = name.lowercase()
    )

    data class Circuit(
        val circuitId: String,
        val name: String,
        val nationality: String,
        val nationalityISO: String,
        val location: String
    ): SearchItem(
        layoutId = R.layout.view_search_circuit,
        searchBy = "${name.lowercase()} ${nationality.lowercase()} ${location.lowercase()}"
    )

    data class Race(
        val raceId: String,
        val season: Int,
        val round: Int,
        val raceName: String,
        val country: String,
        val countryISO: String,
        val circuitName: String,
        val date: LocalDate
    ): SearchItem(
        layoutId = R.layout.view_search_race,
        searchBy = "$round $season ${raceName.lowercase()} ${circuitName.lowercase()} ${country.lowercase()} ${DateTimeFormatter.ofPattern("MMMM").format(date).lowercase()}"
    )

    object Placeholder: SearchItem(
        layoutId = R.layout.view_search_placeholder,
        searchBy = null
    )

    data class ErrorItem(
        val item: SyncDataItem
    ): SearchItem(
        layoutId = item.layoutId,
        searchBy = null
    )
}