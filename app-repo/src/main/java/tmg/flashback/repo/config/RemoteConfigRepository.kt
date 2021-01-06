package tmg.flashback.repo.config

import tmg.flashback.repo.models.remoteconfig.UpNextSchedule

/**
 * Remote config variables and storage data
 *   Every type returned is wrapped in a
 */
abstract class RemoteConfigRepository {

    /**
     * Get a list of all the seasons to show in the list
     */
    val supportedSeasons: Set<Int> get() = supportedSeasonsRC
    protected abstract val supportedSeasonsRC: Set<Int>

    /**
     * What year we should default too when opening the app
     */
    val defaultSeason: Int by lazy { defaultSeasonRC }
    protected abstract val defaultSeasonRC: Int

    /**
     * Up next schedule to be shown in the app
     * - Contains name, a date, an optional time and potentially a flag
     */
    val upNext: List<UpNextSchedule> get() = upNextRC
    protected abstract val upNextRC: List<UpNextSchedule>

    /**
     * Banner to be shown at the top of the home screen
     */
    val banner: String? get() = bannerRC
    protected abstract val bannerRC: String?

    /**
     * Enable the RSS feed functionality
     */
    val rss: Boolean by lazy { rssRC }
    protected abstract val rssRC: Boolean

    /**
     * Data provided by tag
     */
    val dataProvidedBy: String? get() = dataProvidedByRC
    protected abstract val dataProvidedByRC: String?

    /**
     * Enable the search functionality
     */
    val search: Boolean by lazy { searchRC }
    protected abstract val searchRC: Boolean
}