package tmg.flashback.formula1.model

fun RaceQualifyingRound.Companion.model(
    label: RaceQualifyingType = RaceQualifyingType.Q1,
    order: Int = 1,
    results: List<RaceQualifyingResult> = listOf(
        RaceQualifyingResult.model()
    )
): RaceQualifyingRound = RaceQualifyingRound(
    label = label,
    order = order,
    results = results
)