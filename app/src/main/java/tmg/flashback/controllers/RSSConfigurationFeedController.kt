package tmg.flashback.controllers

import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.rss.controllers.RSSFeedController
import tmg.flashback.rss.repo.enums.SupportedArticleSource

class RSSConfigurationFeedController(
    private val configurationController: ConfigurationController
): RSSFeedController() {

    /**
     * Supported sources
     */
    override val supportedSources: List<SupportedArticleSource>
        get() = configurationController.rssSupportedSources.map {
            SupportedArticleSource(
                rssLink = it.rssLink,
                sourceShort = it.sourceShort,
                source = it.source,
                colour = it.colour,
                textColour = it.textColour,
                title = it.title,
                contactLink = it.contactLink,
            )
        }

    /**
     * Show add custom RSS feeds
     */
    override val showAddCustomFeeds: Boolean
        get() = configurationController.rssAddCustom
}