package tmg.configuration.repository.json

import tmg.configuration.repository.models.SupportedSource

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

//region Converters

fun SupportedSourcesJson.convert(): List<SupportedSource> {
    return this.sources?.mapNotNull { it.convert() } ?: emptyList()
}

fun SupportedSourceJson.convert(): SupportedSource? {
    if (this.source == null || this.title == null || this.rssLink == null || this.colour == null || this.textColour == null) {
        return null
    }

    return SupportedSource(
        rssLink = this.rssLink,
        sourceShort = this.sourceShort ?: this.source.take(2),
        source = this.source,
        colour = this.colour,
        textColour = this.textColour,
        title = this.title,
        contactLink = this.contactLink ?: this.source,
    )
}

//endregion