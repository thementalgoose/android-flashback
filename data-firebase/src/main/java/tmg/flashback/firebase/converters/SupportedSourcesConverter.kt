package tmg.flashback.firebase.converters

import tmg.flashback.firebase.models.FSupportedSource
import tmg.flashback.firebase.models.FSupportedSources
import tmg.flashback.data.models.remoteconfig.SupportedArticleSource

fun FSupportedSources.convert(): List<SupportedArticleSource> {
    return this.sources?.mapNotNull { it.convert() } ?: emptyList()
}

fun FSupportedSource.convert(): SupportedArticleSource? {
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