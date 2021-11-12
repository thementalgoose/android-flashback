package tmg.flashback.formula1.model

fun RaceQualifyingRound.Companion.model(
    label: String = "Q1",
    order: Int = 1,
    results: List<RaceQualifyingRoundDriver> = listOf(

    )
): RaceQualifyingRound = RaceQualifyingRound(
    label = label,
    order = order,
    results = results
)