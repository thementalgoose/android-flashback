package tmg.flashback.debug.noop.manager

import tmg.flashback.debug.manager.BaseUrlLocalOverrideManager

class NoopBaseUrlLocalOverrideManager: BaseUrlLocalOverrideManager {
    override var localBaseUrl: String?
        get() = null
        set(value) { /* no-op */ }
}