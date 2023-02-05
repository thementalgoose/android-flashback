package tmg.flashback.maintenance.repository

import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.maintenance.repository.converters.convert
import tmg.flashback.maintenance.repository.json.ForceUpgradeJson
import tmg.flashback.maintenance.repository.model.ForceUpgrade
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaintenanceRepository @Inject constructor(
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