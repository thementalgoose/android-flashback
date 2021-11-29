package tmg.flashback.configuration.manager

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import tmg.flashback.configuration.services.RemoteConfigService
import tmg.flashback.crash_reporting.controllers.CrashController

class ConfigManager(
    private val configService: RemoteConfigService,
    private val crashController: CrashController
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
    fun <T> getJson(key: String, serializer: KSerializer<T>): T? {
        val string = configService.getString(key)
        val obj = try {
            Json.decodeFromString(serializer, string)
        } catch (e: SerializationException) {
            crashController.logException(e, "Failed to deserialize remote config value $key - $string")
            null
        }
        return obj
    }
}