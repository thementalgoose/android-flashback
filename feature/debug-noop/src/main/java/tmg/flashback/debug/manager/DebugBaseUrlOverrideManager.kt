package tmg.flashback.debug.manager

import tmg.flashback.statistics.network.manager.BaseUrlLocalOverrideManager
import javax.inject.Inject

class DebugBaseUrlOverrideManager @Inject constructor(): BaseUrlLocalOverrideManager {
    override var localBaseUrl: String?
        get() = null
        set(value) { /* Do nothing */ }
}