package tmg.flashback.debug.noop

import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.model.DebugMenuItem

class NoopDebugNavigationComponent: DebugNavigationComponent {
    override fun navigateTo(id: String) { /* no-op */ }
    override fun getDebugMenuItems(): List<DebugMenuItem> = emptyList()
}