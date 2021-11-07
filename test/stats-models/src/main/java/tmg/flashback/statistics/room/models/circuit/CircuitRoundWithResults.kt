package tmg.flashback.statistics.room.models.circuit

fun CircuitRoundWithResults.Companion.model(
    round: CircuitRound = CircuitRound.model(),
    // TODO: Circuit results preview - Map this properly
    results: List<CircuitRoundResultWithDriverConstructor> = emptyList()
): CircuitRoundWithResults = CircuitRoundWithResults(
    round = round,
    results = results
)