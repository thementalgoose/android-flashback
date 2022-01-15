package tmg.flashback.configuration.controllers

import tmg.flashback.configuration.constants.Migrations
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.services.RemoteConfigService

/**
 * Remote config variables and storage data
 *   Every type returned is wrapped in a
 */
class ConfigController(
    private val configRepository: ConfigRepository,
    private val configService: RemoteConfigService
) {

    init {
        configService.initialiseRemoteConfig()
    }

    /**
     * Does the on device configuration require a synchronisation
     */
    val requireSynchronisation: Boolean
        get() = Migrations.configurationSyncCount != configRepository.remoteConfigSync

    /**
     * Reset the cache if it hasn't been refreshed
     */
    suspend fun ensureCacheReset(): Boolean {
        val existing = configRepository.resetAtMigrationVersion
        if (existing != Migrations.configurationSyncCount) {
            configRepository.resetAtMigrationVersion = Migrations.configurationSyncCount
            configService.reset()
        }
        return true
    }

    /**
     * Fetch the latest configuration setup for the app.
     *  This will not be applied until [fetchAndApply] or [applyPending] is called
     */
    suspend fun fetch(): Boolean {
        return configService.fetch(false)
    }

    /**
     * Fetch the latest configuration and apply it immediately. Returns true if update is found and applied, false otherwise
     *  Resets the config count to bypass this as a failsafe?
     */
    suspend fun fetchAndApply(): Boolean {
        val result = configService.fetch(true)
        if (result) {
            configRepository.remoteConfigSync = Migrations.configurationSyncCount
        }
        return result
    }

    /**
     * Reset the local cache
     */
    suspend fun reset(): Boolean {
        configRepository.remoteConfigSync = 0
        return configService.reset()
    }

    /**
     * Apply the latest version of the configuration for the app
     * - If one was fetched in the background and has not yet been applied, then apply it here. Returning true
     * - If no update is found, this step just returns false
     */
    suspend fun applyPending(): Boolean {
        val result = configService.activate()
        if (result) {
            configRepository.remoteConfigSync = Migrations.configurationSyncCount
        }
        return result
    }
}