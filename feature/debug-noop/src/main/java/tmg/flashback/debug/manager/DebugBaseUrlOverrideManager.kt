package tmg.flashback.debug.manager

import tmg.flashback.statistics.network.manager.BaseUrlLocalOverrideManager

class DebugBaseUrlOverrideManager: BaseUrlLocalOverrideManager {
    override var localBaseUrl: String?
        get() = null
        set(value) { /* Do nothing */ }
}