package tmg.flashback.statistics.network.models.circuits

fun Circuit.Companion.model(
    data: CircuitData = CircuitData.model(),
    results: Map<String, CircuitResult>? = mapOf(
        "circuitId" to CircuitResult.model()
    )
): Circuit = Circuit(
    data = data,
    results = results
)





