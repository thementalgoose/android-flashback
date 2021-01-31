package tmg.flashback.core.repositories

import tmg.flashback.core.model.ForceUpgrade
import tmg.flashback.core.model.SupportedSource
import tmg.flashback.core.model.UpNextSchedule

/**
 * Remote config variables and storage data
 *   Every type returned is wrapped in a
 */
interface ConfigurationRepository {

    /**
     * Get a list of all the seasons to show in the list
     */
    val supportedSeasons: Set<Int>

    /**
     * What year we should default too when opening the app
     */
    val defaultSeason: Int

    /**
     * Up next schedule to be shown in the app
     * - Contains name, a date, an optional time and potentially a flag
     */
    val upNext: List<UpNextSchedule>

    /**
     * Banner to be shown at the top of the home screen
     */
    val banner: String?

    /**
     * Force upgrade model
     */
    val forceUpgrade: ForceUpgrade?

    /**
     * Data provided by tag
     */
    val dataProvidedBy: String?

    /**
     * Enable the search functionality
     */
    val search: Boolean

    //region RSS

    /**
     * Enable the RSS feed functionality
     */
    val rss: Boolean

    /**
     * Enables the ability to add custom RSS feeds
     */
    val rssAddCustom: Boolean

    /**
     * List of supported articles for the RSS configure functionality
     */
    val rssSupportedSources: List<SupportedSource>

    //endregion
}
