package tmg.flashback.debug.core.manager

import tmg.flashback.debug.manager.BaseUrlLocalOverrideManager
import tmg.flashback.prefs.manager.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DebugBaseUrlOverrideManager @Inject constructor(
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