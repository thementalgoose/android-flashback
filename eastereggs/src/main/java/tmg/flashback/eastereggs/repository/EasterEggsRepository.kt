package tmg.flashback.eastereggs.repository

import tmg.flashback.configuration.manager.ConfigManager
import javax.inject.Inject

class EasterEggsRepository @Inject constructor(
    private val configManager: ConfigManager
) {

    companion object {
        private const val keySnow = "easteregg_snow"
        private const val keyUkraine = "easteregg_ukraine"
    }

    internal val isSnowEnabled: Boolean
        get() = configManager.getBoolean(keySnow)

    internal val isUkraineEnabled: Boolean
        get() = configManager.getBoolean(keyUkraine)
}