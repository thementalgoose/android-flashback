package tmg.flashback.core.controllers

import tmg.flashback.core.constants.Migrations
import tmg.flashback.core.managers.ConfigurationManager
import tmg.flashback.core.model.ForceUpgrade
import tmg.flashback.core.model.SupportedSource
import tmg.flashback.core.model.UpNextSchedule
import tmg.flashback.core.repositories.ConfigurationRepository
import tmg.flashback.core.repositories.CoreRepository

/**
 * Remote config variables and storage data
 *   Every type returned is wrapped in a
 */
class ConfigurationController(
    private val configurationRepository: ConfigurationRepository,
    private val coreRepository: CoreRepository,
    private val configurationManager: ConfigurationManager
) {

    //region Manage stuff

    init {
        configurationManager.setDefaults()
    }

    /**
     * Does the on device configuration require a synchronisation
     */
    val requireSynchronisation: Boolean
        get() = Migrations.configurationSyncCount != coreRepository.remoteConfigSync

    /**
     * Fetch the latest configuration setup for the app.
     *  This will not be applied until [fetchAndApply] or [applyPending] is called
     */
    suspend fun fetch(): Boolean {
        return configurationManager.update(false)
    }

    /**
     * Fetch the latest configuration and apply it immediately. Returns true if update is found and applied, false otherwise
     */
    suspend fun fetchAndApply(): Boolean {
        val result = configurationManager.update(true)
        coreRepository.remoteConfigSync = Migrations.configurationSyncCount
        return result
    }

    /**
     * Apply the latest version of the configuration for the app
     * - If one was fetched in the background and has not yet been applied, then apply it here. Returning true
     * - If no update is found, this step just returns false
     */
    suspend fun applyPending(): Boolean {
        return configurationManager.activate()
    }

    //endregion

    //region Variables

    /**
     * Get a list of all the seasons to show in the list
     */
    val supportedSeasons: Set<Int> get() = configurationRepository.supportedSeasons

    /**
     * What year we should default too when opening the app
     *  This is the one flashback determines is the best to show
     */
    val defaultSeason: Int by lazy { configurationRepository.defaultSeason }

    /**
     * Up next schedule to be shown in the app
     * - Contains name, a date, an optional time and potentially a flag
     */
    val upNext: List<UpNextSchedule> get() = configurationRepository.upNext

    /**
     * Banner to be shown at the top of the home screen
     */
    val banner: String? get() = configurationRepository.banner

    /**
     * Force upgrade message to be shown
     * {
     *   "title": "Upgrade now",
     *   "message": "Message to explain the decision",
     *   "link": "https://www.google.com"
     *   "linkText": "Go to play store"
     * }
     */
    val forceUpgrade: ForceUpgrade? by lazy { configurationRepository.forceUpgrade }

    /**
     * Data provided by tag
     *  Text to be displayed on every statistics screen of who the statistics are provided by
     */
    val dataProvidedBy: String? get() = configurationRepository.dataProvidedBy

    /**
     * The new calendar tab in the dashboard should be enabled or not
     */
    val dashboardCalendar: Boolean by lazy { configurationRepository.dashboardCalendar }

    //endregion

    //region Variables - RSS

    /**
     * Enable the RSS feed functionality
     */
    val rss: Boolean by lazy { configurationRepository.rss }

    /**
     * Enables the ability to add custom RSS feeds
     *  Section gets removed from the RSS screen but custom URLs are still listed in the users preferences
     */
    val rssAddCustom: Boolean by lazy { configurationRepository.rssAddCustom }

    /**
     * List of supported articles for the RSS configure functionality
     */
    val rssSupportedSources: List<SupportedSource> by lazy { configurationRepository.rssSupportedSources }

    //endregion
}
