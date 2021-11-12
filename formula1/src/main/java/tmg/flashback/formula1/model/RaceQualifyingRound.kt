package tmg.flashback.formula1.model

data class RaceQualifyingRound(
    val label: String,
    val order: Int,
    val results: List<RaceQualifyingRoundDriver>
) {
    companion object
}

fun List<RaceQualifyingRound>.finalOrder(): List<RaceQualifyingRoundDriver> {
    return this.maxByOrNull { it.order }?.results ?: emptyList()
}