package tmg.flashback.statistics.ui.search

import androidx.annotation.LayoutRes
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem

sealed class SearchItem(
    @LayoutRes
    val layoutId: Int
) {
    data class Driver(
        val driverId: String
    ): SearchItem(R.layout.view_search_driver)

    data class Constructor(
        val constructorId: String
    ): SearchItem(R.layout.view_search_constructor)

    data class Circuit(
        val circuitId: String
    ): SearchItem(R.layout.view_search_circuit)

    data class Race(
        val raceId: String
    ): SearchItem(R.layout.view_search_race)

    data class ErrorItem(
        val item: SyncDataItem
    ): SearchItem(item.layoutId)
}