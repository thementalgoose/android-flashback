package tmg.flashback.stats.repository.json

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class BannerJson(
    val banners: List<BannerItemJson>?
)

@Keep
@Serializable
data class BannerItemJson(
    val msg: String?,
    val url: String?,
    val highlight: Boolean?,
    val season: Int?
)