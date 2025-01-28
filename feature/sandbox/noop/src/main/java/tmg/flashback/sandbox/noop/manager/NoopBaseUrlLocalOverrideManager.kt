package tmg.flashback.sandbox.noop.manager

import tmg.flashback.sandbox.manager.BaseUrlLocalOverrideManager
import javax.inject.Inject

internal class NoopBaseUrlLocalOverrideManager @Inject constructor(): BaseUrlLocalOverrideManager {
    override var localBaseUrl: String?
        get() = null
        set(value) { /* no-op */ }
}