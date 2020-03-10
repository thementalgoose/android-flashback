package tmg.f1stats.home.season

data class SeasonRaceModel(
    val season: Int,
    val round: Int,
    val raceKey: String,
    val raceName: String,
    val circuitName: String
)