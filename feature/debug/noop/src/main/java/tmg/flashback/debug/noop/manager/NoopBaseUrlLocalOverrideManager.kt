package tmg.flashback.debug.noop.manager

import tmg.flashback.debug.manager.BaseUrlLocalOverrideManager
import javax.inject.Inject

internal class NoopBaseUrlLocalOverrideManager @Inject constructor(): BaseUrlLocalOverrideManager {
    override var localBaseUrl: String?
        get() = null
        set(value) { /* no-op */ }
}