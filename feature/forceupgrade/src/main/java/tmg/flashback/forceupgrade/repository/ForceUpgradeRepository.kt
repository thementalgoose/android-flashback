package tmg.flashback.forceupgrade.repository

import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.forceupgrade.repository.converters.convert
import tmg.flashback.forceupgrade.repository.json.ForceUpgradeJson
import tmg.flashback.forceupgrade.repository.model.ForceUpgrade
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForceUpgradeRepository @Inject constructor(
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
    internal val forceUpgrade: ForceUpgrade?
        get() = configManager
                .getJson(keyForceUpgrade, ForceUpgradeJson.serializer())
                ?.convert()

    val shouldForceUpgrade: Boolean
        get() = forceUpgrade != null
}