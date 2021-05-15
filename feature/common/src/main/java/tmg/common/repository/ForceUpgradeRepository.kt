package tmg.common.repository

import tmg.common.repository.converters.convert
import tmg.common.repository.json.ForceUpgradeJson
import tmg.configuration.controllers.ConfigController
import tmg.common.repository.model.ForceUpgrade

class ForceUpgradeRepository(
        private val configController: ConfigController
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
        get() = configController
                .getJson<ForceUpgradeJson>(keyForceUpgrade)
                ?.convert()
}