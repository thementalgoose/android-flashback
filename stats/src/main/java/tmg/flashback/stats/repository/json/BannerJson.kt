package tmg.flashback.stats.repository.json

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class BannerJson(
    val msg: String?,
    val url: String?
)