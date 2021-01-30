package tmg.flashback.rss.prefs

import tmg.flashback.rss.repo.enums.SupportedArticleSource

interface RSSConfiguration {

    /**
     * List of all the supported article sources to be shown in the app
     */
    val supportedArticles: List<SupportedArticleSource>
}