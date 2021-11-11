package tmg.flashback.rss.repo.json

data class SupportedSourcesJson(
    val sources: List<SupportedSourceJson>? = null
)

data class SupportedSourceJson(
    val rssLink: String? = null,
    val sourceShort: String? = null,
    val source: String? = null,
    val colour: String? = null,
    val textColour: String? = null,
    val title: String? = null,
    val contactLink: String? = null
)