package tmg.flashback.circuits.presentation

import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.CircuitHistoryRace

data class CircuitScreenState(
    val circuitId: String,
    val circuitName: String,
    val circuit: Circuit? = null,
    val list: List<CircuitModel> = emptyList(),
    val isLoading: Boolean = false,
    val networkAvailable: Boolean = false,
    val selectedRace: CircuitHistoryRace? = null
)