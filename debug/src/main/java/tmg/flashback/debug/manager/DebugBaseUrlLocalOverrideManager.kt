package tmg.flashback.debug.manager

import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.statistics.network.manager.BaseUrlLocalOverrideManager

class DebugBaseUrlLocalOverrideManager(
    private val preferenceManager: PreferenceManager
) : BaseUrlLocalOverrideManager {

    companion object {
        private const val keyOverrideBaseUrl: String = "debug_override_url"
    }

    override var localBaseUrl: String?
        get() {
            val result = preferenceManager.getString(keyOverrideBaseUrl, "")
            if (result.isNullOrBlank()) {
                return null
            }
            return result
        }
        set(value) {
            preferenceManager.save(keyOverrideBaseUrl, value ?: "")
        }
}