package tmg.flashback.statistics.repository.json

import kotlinx.serialization.Serializable

@Serializable
data class BannerJson(
    val msg: String?,
    val url: String?
)