package tmg.f1stats.repo.models

data class History(
    val season: Int,
    val round: List<HistoryRound>
)

data class HistoryRound(
    val season: Int,
    val round: Int,
    val raceName: String,
    val circuitName: String,
    val country: String,
    val countryISO: String
)