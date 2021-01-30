package tmg.flashback.core.model

data class SupportedArticleSource(
    val rssLink: String,
    val sourceShort: String,
    val source: String,
    val colour: String,
    val textColour: String,
    val title: String,
    val contactLink: String = source
)