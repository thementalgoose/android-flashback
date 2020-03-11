package tmg.f1stats.season

data class SeasonAdapterModel(
    val season: Int,
    val round: Int,
    val raceKey: String,
    val raceName: String,
    val circuitName: String
)