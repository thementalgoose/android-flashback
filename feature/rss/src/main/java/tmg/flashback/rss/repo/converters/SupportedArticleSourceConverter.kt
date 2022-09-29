package tmg.flashback.rss.repo.converters

import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.repo.model.SupportedSource

fun SupportedSource.toArticleSource(): SupportedArticleSource {
    return SupportedArticleSource(
         rssLink = this.rssLink,
         sourceShort = this.sourceShort,
         source = this.source,
         colour = this.colour,
         textColour = this.textColour,
         title = this.title,
         contactLink = this.contactLink,
    )
}