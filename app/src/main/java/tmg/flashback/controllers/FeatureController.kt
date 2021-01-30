package tmg.flashback.controllers

import tmg.flashback.core.controllers.ConfigurationController

/**
 * All major features in the app
 */
class FeatureController(
        private val configurationController: ConfigurationController
) {
    /**
     * Is the RSS feature enabled
     */
    val rssEnabled: Boolean
        get() = configurationController.rss

    /**
     * Is the Search feature enabled
     */
    val searchEnabled: Boolean
        get() = false // remoteConfigRepository.search

}