package tmg.flashback.statistics.repository.json

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AllSeasonsJson(
    val seasons: List<AllSeasonJson>? = null
)

@Keep
@Serializable
data class AllSeasonJson(
    val s: Int?
)