package tmg.flashback.statistics.ui.search

import androidx.annotation.LayoutRes
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
        val constructorId: String
    ): SearchItem(
        layoutId = R.layout.view_search_constructor,
        searchBy = constructorId.lowercase()
    )

    data class Circuit(
        val circuitId: String
    ): SearchItem(
        layoutId = R.layout.view_search_circuit,
        searchBy = circuitId.lowercase()
    )

    data class Race(
        val raceId: String
    ): SearchItem(
        layoutId = R.layout.view_search_race,
        searchBy = raceId.lowercase()
    )

    data class ErrorItem(
        val item: SyncDataItem
    ): SearchItem(
        layoutId = item.layoutId,
        searchBy = null
    )
}