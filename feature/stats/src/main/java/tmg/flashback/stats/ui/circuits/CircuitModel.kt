package tmg.flashback.stats.ui.circuits

import tmg.flashback.formula1.model.CircuitHistoryRace

sealed class CircuitModel(
    val id: String
) {
    data class CircuitItem(
        val data: CircuitHistoryRace
    ): CircuitModel(
        id = "${data.season}-${data.round}"
    )
}