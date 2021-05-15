package tmg.common.repository

import tmg.common.repository.converters.convert
import tmg.common.repository.json.ForceUpgradeJson
import tmg.configuration.controllers.ConfigController
import tmg.common.repository.model.ForceUpgrade
import tmg.configuration.manager.ConfigManager

class ForceUpgradeRepository(
        private val configManager: ConfigManager
) {
    companion object {
        private const val keyForceUpgrade: String = "force_upgrade"
    }

    /**
     * Force upgrade message to be shown
     * {
     *   "title": "Upgrade now",
     *   "message": "Message to explain the decision",
     *   "link": "https://www.google.com"
     *   "linkText": "Go to play store"
     * }
     */
    val forceUpgrade: ForceUpgrade?
        get() = configManager
                .getJson<ForceUpgradeJson>(keyForceUpgrade)
                ?.convert()
}