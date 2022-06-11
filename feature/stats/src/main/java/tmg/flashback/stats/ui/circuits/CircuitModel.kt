package tmg.flashback.stats.ui.circuits

import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.Location

sealed class CircuitModel(
    val id: String
) {
    data class Stats(
        val circuitId: String,
        val name: String,
        val nationality: String,
        val nationalityISO: String,
        val numberOfGrandPrix: Int,
        val startYear: Int,
        val endYear: Int,
        val wikipedia: String?,
        val location: Location?
    ): CircuitModel(
        id = "stats"
    )

    data class Item(
        val data: CircuitHistoryRace
    ): CircuitModel(
        id = "${data.season}-${data.round}"
    )
}