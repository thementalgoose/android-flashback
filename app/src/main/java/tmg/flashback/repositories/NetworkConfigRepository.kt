package tmg.flashback.repositories

import tmg.flashback.BuildConfig
import tmg.flashback.configuration.manager.ConfigManager

class NetworkConfigRepository(
    private val configManager: ConfigManager
) {

    companion object {
        private const val keyConfigUrl: String = "config_url"

        private const val defaultUrl: String = BuildConfig.BASE_URL
    }

    val configUrl: String
        get() = configManager.getString(keyConfigUrl) ?: defaultUrl
}