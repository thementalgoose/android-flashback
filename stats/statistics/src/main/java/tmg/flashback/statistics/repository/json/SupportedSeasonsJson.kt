package tmg.flashback.statistics.repository.json

data class AllSeasonsJson(
    val seasons: List<AllSeasonJson>? = null
)

data class AllSeasonJson(
    val s: Int?
)