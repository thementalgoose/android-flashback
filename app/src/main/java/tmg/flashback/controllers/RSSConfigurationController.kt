package tmg.flashback.controllers

import tmg.flashback.data.config.RemoteConfigRepository
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.rss.repo.enums.SupportedArticleSource

class RSSConfigurationController(
    private val remoteConfigRepository: RemoteConfigRepository
): RSSController() {

    /**
     * Supported sources
     */
    override val supportedSources: List<SupportedArticleSource>
        get() = remoteConfigRepository.rssSupportedSources.map {
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
        get() = remoteConfigRepository.rssAddCustom
}