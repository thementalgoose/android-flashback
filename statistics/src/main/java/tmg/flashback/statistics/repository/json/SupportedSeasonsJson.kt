package tmg.flashback.statistics.repository.json

import kotlinx.serialization.Serializable

@Serializable
data class AllSeasonsJson(
    val seasons: List<AllSeasonJson>? = null
)

@Serializable
data class AllSeasonJson(
    val s: Int?
)