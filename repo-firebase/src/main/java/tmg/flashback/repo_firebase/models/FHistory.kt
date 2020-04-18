package tmg.flashback.repo_firebase.models

data class FHistorySeason(
    val all: Map<String, Map<String, FHistorySeasonRound>> = mapOf()
)

data class FHistorySeasonRound(
    val round: Int = -1,
    val season: Int = -1,
    val country: String = "",
    val countryISO: String = "",
    val circuit: String = "",
    val raceName: String = ""
)