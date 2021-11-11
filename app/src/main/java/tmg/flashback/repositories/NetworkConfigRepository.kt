package tmg.flashback.repositories

import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.BuildConfig

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