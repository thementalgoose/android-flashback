package tmg.flashback.stats.ui.circuits

import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.formula1.model.CircuitHistoryRace

sealed class CircuitModel(
    val id: String
) {
    data class Item(
        val data: CircuitHistory
    ): CircuitModel(
        id = "${data.season}-${data.round}"
    )
}