package tmg.flashback.stats.ui.circuits

import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.Location

sealed class CircuitModel(
    val id: String
) {
    data class Stats(
        val circuitId: String,
        val name: String,
        val country: String,
        val countryISO: String,
        val numberOfGrandPrix: Int,
        val startYear: Int?,
        val endYear: Int?,
        val wikipedia: String?,
        val location: Location?
    ): CircuitModel(
        id = "stats"
    ) {
        companion object
    }

    data class Item(
        val circuitId: String,
        val circuitName: String,
        val country: String,
        val countryISO: String,
        val data: CircuitHistoryRace
    ): CircuitModel(
        id = "${data.season}-${data.round}"
    ) {
        companion object
    }

    object Loading: CircuitModel(
        id = "loading"
    )

    object Error: CircuitModel(
        id = "error"
    )
}