package tmg.flashback.rss.repo.converters

import tmg.flashback.rss.repo.json.SupportedSourceJson
import tmg.flashback.rss.repo.json.SupportedSourcesJson
import tmg.flashback.rss.repo.model.SupportedSource

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