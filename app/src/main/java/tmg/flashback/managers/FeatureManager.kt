package tmg.flashback.managers

import org.threeten.bp.LocalDate
import tmg.flashback.repo.config.RemoteConfigRepository

/**
 * All major features in the app
 */
class FeatureManager(
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