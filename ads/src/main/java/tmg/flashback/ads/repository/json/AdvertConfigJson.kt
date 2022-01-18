package tmg.flashback.ads.repository.json

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AdvertConfigJson(
    val locations: AdvertLocationJson? = null,
    val allowUserConfig: Boolean? = null
)

@Keep
@Serializable
data class AdvertLocationJson(
    val home: Boolean? = null,
    val race: Boolean? = null,
    val rss: Boolean? = null,
    val search: Boolean? = null
)