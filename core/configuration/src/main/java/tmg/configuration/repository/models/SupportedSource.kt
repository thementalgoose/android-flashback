package tmg.configuration.repository.models

data class SupportedSource(
    val rssLink: String,
    val sourceShort: String,
    val source: String,
    val colour: String,
    val textColour: String,
    val title: String,
    val contactLink: String = source
)