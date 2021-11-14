package tmg.flashback.formula1.model

data class RaceQualifyingRound(
    val label: RaceQualifyingType,
    val order: Int,
    val results: List<RaceQualifyingRoundDriver>
) {
    val isSprintQualifying: Boolean by lazy {
        results.any { it is RaceQualifyingRoundDriver.SprintQualifying }
    }

    companion object
}