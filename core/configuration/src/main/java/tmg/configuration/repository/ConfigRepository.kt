package tmg.configuration.repository

import org.threeten.bp.Year
import tmg.configuration.extensions.toJson
import tmg.configuration.managers.RemoteConfigManager
import tmg.configuration.repository.json.*
import tmg.configuration.repository.models.ForceUpgrade
import tmg.configuration.repository.models.SupportedSource
import tmg.configuration.repository.models.UpNextSchedule
import tmg.core.prefs.manager.PreferenceManager

class ConfigRepository(
    private val remoteConfigManager: RemoteConfigManager,
    private val preferenceManager: PreferenceManager
) {

    companion object {
        private const val keyRemoteConfigSync: String = "REMOTE_CONFIG_SYNC_COUNT"

        private const val keyDefaultYear: String = "default_year"
        private const val keyUpNext: String = "up_next"
        private const val keyDefaultBanner: String = "banner"
        private const val keyForceUpgrade: String = "force_upgrade"
        private const val keyDataProvidedBy: String = "data_provided"
        private const val keySupportedSeasons: String = "supported_seasons"
        private const val keyDashboardCalendar: String = "dashboard_calendar"
        private const val keyRss: String = "rss"
        private const val keyRssAddCustom: String = "rss_add_custom"
        private const val keyRssSupportedSources: String = "rss_supported_sources"
    }

    //region Shared Prefs

    var remoteConfigSync: Int
        get() = preferenceManager.getInt(keyRemoteConfigSync, 0)
        set(value) = preferenceManager.save(keyRemoteConfigSync, value)

    //endregion

    //region Remote Config values

    /**
     * Get a list of all the seasons to show in the list
     */
    val supportedSeasons: Set<Int>
        get() = remoteConfigManager
            .getString(keySupportedSeasons)
            .toJson<AllSeasonsJson>()
            ?.convert()
            ?: emptySet()

    /**
     * What year we should default too when opening the app
     *  This is the one flashback determines is the best to show
     */
    val defaultSeason: Int by lazy {
        remoteConfigManager
            .getString(keyDefaultYear).toIntOrNull() ?: Year.now().value
    }

    /**
     * Banner to be shown at the top of the home screen
     */
    /**
     * Banner to be shown at the top of the home screen
     */
    val banner: String
        get() = remoteConfigManager.getString(keyDefaultBanner)

    /**
     * Force upgrade message to be shown
     * {
     *   "title": "Upgrade now",
     *   "message": "Message to explain the decision",
     *   "link": "https://www.google.com"
     *   "linkText": "Go to play store"
     * }
     */
    val forceUpgrade: ForceUpgrade?
        get() = remoteConfigManager
            .getString(keyForceUpgrade)
            .toJson<ForceUpgradeJson>()
            ?.convert()

    /**
     * Data provided by tag
     *  Text to be displayed on every statistics screen of who the statistics are provided by
     */
    val dataProvidedBy: String
        get() = remoteConfigManager.getString(keyDataProvidedBy)

    /**
     * The new calendar tab in the dashboard should be enabled or not
     */
    val dashboardCalendar: Boolean by lazy {
        remoteConfigManager.getBoolean(keyDashboardCalendar)
    }

    /**
     * Up next schedule to be shown in the app
     * - Contains name, a date, an optional time and potentially a flag
     */
    val upNext: List<UpNextSchedule>
        get() = remoteConfigManager
            .getString(keyUpNext)
            .toJson<UpNextJson>()
            ?.convert()
            ?: emptyList()

    /**
     * Enable the RSS feed functionality
     */
    val rss: Boolean by lazy {
        remoteConfigManager.getBoolean(keyRss)
    }

    /**
     * Enables the ability to add custom RSS feeds
     *  Section gets removed from the RSS screen but custom URLs are still listed in the users preferences
     */
    val rssAddCustom: Boolean by lazy {
        remoteConfigManager.getBoolean(keyRssAddCustom)
    }

    /**
     * List of supported articles for the RSS configure functionality
     */
    val rssSupportedSources: List<SupportedSource> by lazy {
        remoteConfigManager
            .getString(keyRssSupportedSources)
            .toJson<SupportedSourcesJson>()
            ?.convert()
            ?: emptyList()
    }

    //endregion
}