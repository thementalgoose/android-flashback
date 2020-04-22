package tmg.flashback.repo.models

data class History(
    val season: Int,
    val rounds: List<HistoryRound>
)

data class HistoryRound(
    val season: Int,
    val round: Int,
    val raceName: String,
    val circuitName: String,
    val country: String,
    val countryISO: String,
    val hasResults: Boolean
)