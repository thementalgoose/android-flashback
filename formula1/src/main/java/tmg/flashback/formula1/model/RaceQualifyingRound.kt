package tmg.flashback.formula1.model

data class RaceQualifyingRound(
    val label: RaceQualifyingType,
    val order: Int,
    val results: List<RaceQualifyingResult>
) {
    val isSprintQualifying: Boolean by lazy {
        results.any { it is RaceQualifyingResult.SprintQualifying }
    }

    companion object
}