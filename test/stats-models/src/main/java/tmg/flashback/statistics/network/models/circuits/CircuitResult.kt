package tmg.flashback.statistics.network.models.circuits

fun CircuitResult.Companion.model(
    race: CircuitResultRace = CircuitResultRace.model(),
    preview: List<CircuitPreviewPosition> = listOf(CircuitPreviewPosition.model())
): CircuitResult = CircuitResult(
    race = race,
    preview = preview
)