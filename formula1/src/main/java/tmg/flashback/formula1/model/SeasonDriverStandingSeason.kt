package tmg.flashback.formula1.model

data class SeasonDriverStandingSeason(
    val season: Int,
    val driver: Driver,
    val points: Double,
    val inProgress: Boolean,
    val races: Int,
    val championshipPosition: Int?,
    val constructors: List<SeasonDriverStandingSeasonConstructor>
)