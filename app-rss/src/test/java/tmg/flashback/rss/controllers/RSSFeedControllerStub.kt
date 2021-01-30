package tmg.flashback.rss.controllers

import tmg.flashback.rss.repo.enums.SupportedArticleSource

class RSSFeedControllerStub(
    private val inputShowAddCustom: Boolean = true
): RSSFeedController() {

    override val showAddCustomFeeds: Boolean
        get() = inputShowAddCustom

    override val supportedSources: List<SupportedArticleSource>
        get() = listOf(primary, secondary)

    companion object {
        val primary: SupportedArticleSource = SupportedArticleSource(
            rssLink = "https://www.rss.com",
            sourceShort = "R1",
            source = "RSS feed 1",
            colour = "#181818",
            textColour = "#494949",
            title = "RSS 1",
            contactLink = "https://www.rss.com/contact"
        )
        val secondary: SupportedArticleSource = SupportedArticleSource(
            rssLink = "https://www.rss2.com",
            sourceShort = "R2",
            source = "RSS feed 2",
            colour = "#181818",
            textColour = "#494949",
            title = "RSS 2",
            contactLink = "https://www.rss2.com/contact"
        )
    }
}