package tmg.flashback.statistics.network.models.circuits

fun CircuitHistory.Companion.model(
    data: Circuit = Circuit.model(),
    results: Map<String, CircuitResult>? = mapOf(
        "circuitId" to CircuitResult.model()
    )
): CircuitHistory = CircuitHistory(
    data = data,
    results = results
)





