package tmg.flashback.statistics.network.models.circuits

fun Circuits.Companion.model(
    data: CircuitData = CircuitData.model(),
    results: Map<String, CircuitResult>? = mapOf(
        "circuitId" to CircuitResult.model()
    )
): Circuits = Circuits(
    data = data,
    results = results
)





