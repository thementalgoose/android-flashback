package tmg.flashback.data.persistence.models.circuit

fun CircuitHistory.Companion.model(
    circuit: Circuit = Circuit.model(),
    races: List<CircuitRoundWithResults> = listOf(
        CircuitRoundWithResults.model()
    )
): CircuitHistory = CircuitHistory(
    circuit = circuit,
    races = races
)