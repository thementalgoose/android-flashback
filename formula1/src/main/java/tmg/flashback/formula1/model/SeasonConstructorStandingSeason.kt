package tmg.flashback.formula1.model

data class SeasonConstructorStandingSeason(
    val season: Int,
    val constructor: Constructor,
    val points: Double,
    val inProgress: Boolean,
    val races: Int,
    val championshipPosition: Int?,
    val drivers: List<SeasonConstructorStandingSeasonDriver>
)