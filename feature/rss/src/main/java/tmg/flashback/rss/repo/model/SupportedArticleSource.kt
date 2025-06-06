package tmg.flashback.rss.repo.model

data class SupportedArticleSource(
    val rssLink: String,
    val sourceShort: String,
    val source: String,
    val colour: String,
    val textColour: String,
    val title: String,
    val contactLink: String = source
) {
    val article: ArticleSource
        get() {
            return ArticleSource(
                    title = this.title,
                    colour = this.colour,
                    textColor = this.textColour,
                    source = source,
                    shortSource = this.sourceShort,
                    rssLink = this.rssLink,
                    contactLink = this.contactLink
            )
        }

    companion object
}