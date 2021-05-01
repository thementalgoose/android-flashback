package tmg.configuration.controllers

import tmg.configuration.managers.RemoteConfigManager
import tmg.configuration.repository.ConfigRepository

class ConfigController(
    private val configRepository: ConfigRepository,
    private val configManager: RemoteConfigManager
) {

    /**
     * Initialise the config for the app and set default values in memory
     */
    fun initialise() {

    }

    /**
     * Fetch the latest configuration update
     * @param andActivate Once a fetch has been performed, apply the changes
     */
    suspend fun fetch(andActivate: Boolean = false): Boolean {
        return true
    }

    /**
     * Apply any updates to the configuration that may have been fetched previously.
     */
    suspend fun apply(): Boolean {
        return true
    }

    //region Variables



    //endregion
}