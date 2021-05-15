package tmg.configuration.manager

import tmg.configuration.extensions.toJson
import tmg.configuration.services.RemoteConfigService

class ConfigManager(
    private val configService: RemoteConfigService
) {

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