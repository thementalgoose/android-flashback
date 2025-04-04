package tmg.flashback.providers.model

import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.formula1.model.CircuitHistoryRace

fun CircuitHistory.Companion.model(
    data: Circuit = Circuit.model(),
    results: List<CircuitHistoryRace> = listOf(
        CircuitHistoryRace.model()
    )
): CircuitHistory = CircuitHistory(
    data = data,
    results = results
)