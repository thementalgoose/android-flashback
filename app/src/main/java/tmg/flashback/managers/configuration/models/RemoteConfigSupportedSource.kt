package tmg.flashback.managers.configuration.models

import tmg.flashback.core.model.SupportedArticleSource

data class RemoteConfigSupportedSources(
    val sources: List<RemoteConfigSupportedSource>? = null
)

data class RemoteConfigSupportedSource(
    val rssLink: String? = null,
    val sourceShort: String? = null,
    val source: String? = null,
    val colour: String? = null,
    val textColour: String? = null,
    val title: String? = null,
    val contactLink: String? = null
)

//region Converters

fun RemoteConfigSupportedSources.convert(): List<SupportedArticleSource> {
    return this.sources?.mapNotNull { it.convert() } ?: emptyList()
}

fun RemoteConfigSupportedSource.convert(): SupportedArticleSource? {
    if (this.source == null || this.title == null || this.rssLink == null || this.colour == null || this.textColour == null) {
        return null
    }

    return SupportedArticleSource(
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