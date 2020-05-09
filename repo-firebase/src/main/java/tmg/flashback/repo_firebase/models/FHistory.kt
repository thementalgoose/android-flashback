package tmg.flashback.repo_firebase.models

data class FHistorySeason(
    val all: Map<String, Map<String, FHistorySeasonRound>> = mapOf()
)

data class FHistorySeasonRound(
    val date: String = "",
    val r: Int = -1,
    val s: Int = -1,
    val country: String = "",
    val countryISO: String = "",
    val circuit: String = "",
    val raceName: String = "",
    val hasResults: Boolean? = false
)