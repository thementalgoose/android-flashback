package tmg.flashback.ads.repository

import tmg.configuration.manager.ConfigManager

class AdsRepository(
    private val configManager: ConfigManager
) {

    companion object {
        private const val keyEnabled: String = "adverts"
    }

    val isEnabled: Boolean
        get() = configManager.getBoolean(keyEnabled)
}