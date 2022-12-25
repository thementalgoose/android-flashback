package tmg.flashback.eastereggs.repository

import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.eastereggs.model.MenuKeys
import javax.inject.Inject

class EasterEggsRepository @Inject constructor(
    private val configManager: ConfigManager
) {

    companion object {
        private const val keySnow = "easteregg_snow"
        private const val keyMenuIcon = "easteregg_menuicon"
    }

    internal val isSnowEnabled: Boolean
        get() = configManager.getBoolean(keySnow)

    internal val menuIcon: MenuKeys?
        get() {
            val configKey = configManager.getString(keyMenuIcon)
            return MenuKeys.values().firstOrNull { it.key == configKey }
        }
}