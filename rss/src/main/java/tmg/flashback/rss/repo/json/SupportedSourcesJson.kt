package tmg.flashback.rss.repo.json

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class SupportedSourcesJson(
    val sources: List<SupportedSourceJson>? = null
)

@Keep
@Serializable
data class SupportedSourceJson(
    val rssLink: String? = null,
    val sourceShort: String? = null,
    val source: String? = null,
    val colour: String? = null,
    val textColour: String? = null,
    val title: String? = null,
    val contactLink: String? = null
)