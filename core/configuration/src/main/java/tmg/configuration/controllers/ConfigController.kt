package tmg.configuration.controllers

import tmg.configuration.constants.Migrations
import tmg.configuration.extensions.toJson
import tmg.configuration.services.RemoteConfigService
import tmg.configuration.repository.ConfigRepository

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
     * Fetch the latest configuration setup for the app.
     *  This will not be applied until [fetchAndApply] or [applyPending] is called
     */
    suspend fun fetch(): Boolean {
        return configService.fetch(false)
    }

    /**
     * Fetch the latest configuration and apply it immediately. Returns true if update is found and applied, false otherwise
     */
    suspend fun fetchAndApply(): Boolean {
        val result = configService.fetch(true)
        configRepository.remoteConfigSync = Migrations.configurationSyncCount
        return result
    }

    /**
     * Apply the latest version of the configuration for the app
     * - If one was fetched in the background and has not yet been applied, then apply it here. Returning true
     * - If no update is found, this step just returns false
     */
    suspend fun applyPending(): Boolean {
        return configService.activate()
    }

    /**
     * Get a boolean from the config service
     */
    fun getBoolean(key: String): Boolean {
        return configService.getBoolean(key)
    }

    /**
     * Get a string from the config service
     * If the key is empty it gets transformed to null
     */
    fun getString(key: String): String? {
        val result = configService.getString(key)
        if (result.isEmpty()) {
            return null
        }
        return result
    }

    /**
     * Get a JSON object from the config service
     */
    fun <T> getJson(key: String, clazz: Class<T>): T? {
        return configService.getString(key)
                .toJson(clazz)
    }
    inline fun <reified T> getJson(key: String): T? {
        return getJson(key, T::class.java)
    }
}