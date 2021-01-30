package tmg.flashback.controllers

import tmg.flashback.data.config.RemoteConfigRepository

/**
 * All major features in the app
 */
class FeatureController(
        private val remoteConfigRepository: RemoteConfigRepository
) {
    /**
     * Is the RSS feature enabled
     */
    val rssEnabled: Boolean
        get() = remoteConfigRepository.rss

    /**
     * Is the Search feature enabled
     */
    val searchEnabled: Boolean
        get() = false // remoteConfigRepository.search

}