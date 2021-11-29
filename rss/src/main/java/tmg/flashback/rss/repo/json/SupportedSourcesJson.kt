package tmg.flashback.rss.repo.json

import kotlinx.serialization.Serializable

@Serializable
data class SupportedSourcesJson(
    val sources: List<SupportedSourceJson>? = null
)

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