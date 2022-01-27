package tmg.flashback.formula1.model

data class SeasonConstructorStandingSeason(
    val season: Int,
    val constructor: Constructor,
    val points: Double,
    val inProgress: Boolean,
    val inProgressName: String?,
    val inProgressRound: Int?,
    val races: Int,
    val championshipPosition: Int?,
    val drivers: List<SeasonConstructorStandingSeasonDriver>
) {

    val hasValidChampionshipPosition: Boolean by lazy {
        when (championshipPosition) {
            null -> false
            0 -> false
            else -> true
        }
    }

    companion object
}