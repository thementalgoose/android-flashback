package tmg.flashback.managers

import tmg.flashback.statistics.network.manager.BaseUrlLocalOverrideManager

class AppBaseUrlLocalOverrideManager: BaseUrlLocalOverrideManager {
    override var localBaseUrl: String?
        get() = null
        set(value) { /* Do nothing */ }
}