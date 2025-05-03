package tmg.flashback.maintenance.repository

import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.maintenance.data.convert
import tmg.flashback.maintenance.data.models.ForceUpgradeDto
import tmg.flashback.maintenance.repository.model.ForceUpgrade
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaintenanceRepository @Inject constructor(
    private val configManager: ConfigManager
) {
    companion object {
        private const val keyForceUpgrade: String = "force_upgrade"
        private const val keySoftUpgrade: String = "soft_upgrade"
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
            .getJson(keyForceUpgrade, ForceUpgradeDto.serializer())
            ?.convert()

    /**
     * Soft upgrade message to be shown
     */
    internal val softUpgrade: Boolean
        get() = configManager
            .getBoolean(keySoftUpgrade)
}