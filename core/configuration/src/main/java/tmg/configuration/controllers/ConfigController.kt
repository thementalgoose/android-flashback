package tmg.configuration.controllers

import tmg.configuration.constants.Migrations
import tmg.configuration.managers.RemoteConfigManager
import tmg.configuration.repository.ConfigRepository
import tmg.configuration.repository.models.ForceUpgrade
import tmg.configuration.repository.models.SupportedSource
import tmg.configuration.repository.models.UpNextSchedule

/**
 * Remote config variables and storage data
 *   Every type returned is wrapped in a
 */
class ConfigController(
    private val configRepository: ConfigRepository,
    private val configManager: RemoteConfigManager
) {

    init {
        configManager.initialiseRemoteConfig()
    }

    /**
     * Does the on device configuration require a synchronisation
     */
    val requireSynchronisation: Boolean
        get() = Migrations.configurationSyncCount != configRepository.remoteConfigSync

    /**
     * Fetch the latest configuration setup for the app.
     *  This will not be applied until [fetchAndApply] or [applyPending] is called
     */
    suspend fun fetch(): Boolean {
        return configManager.fetch(false)
    }

    /**
     * Fetch the latest configuration and apply it immediately. Returns true if update is found and applied, false otherwise
     */
    suspend fun fetchAndApply(): Boolean {
        val result = configManager.fetch(true)
        configRepository.remoteConfigSync = Migrations.configurationSyncCount
        return result
    }

    /**
     * Apply the latest version of the configuration for the app
     * - If one was fetched in the background and has not yet been applied, then apply it here. Returning true
     * - If no update is found, this step just returns false
     */
    suspend fun applyPending(): Boolean {
        return configManager.activate()
    }

    //region Variables

    val supportedSeasons: Set<Int> get() = configRepository.supportedSeasons

    val defaultSeason: Int get() = configRepository.defaultSeason

    val banner: String get() = configRepository.banner

    val forceUpgrade: ForceUpgrade? get() = configRepository.forceUpgrade

    val dashboardCalendar: Boolean get() = configRepository.dashboardCalendar

    val dataProvidedBy: String get() = configRepository.dataProvidedBy

    val upNext: List<UpNextSchedule> get() = configRepository.upNext

    val rss: Boolean get() = configRepository.rss

    val rssAddCustom: Boolean get() = configRepository.rssAddCustom

    val rssSupportedSources: List<SupportedSource> get() = configRepository.rssSupportedSources

    //endregion
}