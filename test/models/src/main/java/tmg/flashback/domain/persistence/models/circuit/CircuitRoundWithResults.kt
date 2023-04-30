package tmg.flashback.domain.persistence.models.circuit

fun CircuitRoundWithResults.Companion.model(
    round: CircuitRound = CircuitRound.model(),
    results: List<CircuitRoundResultWithDriverConstructor> = listOf(CircuitRoundResultWithDriverConstructor.model())
): CircuitRoundWithResults = CircuitRoundWithResults(
    round = round,
    results = results
)