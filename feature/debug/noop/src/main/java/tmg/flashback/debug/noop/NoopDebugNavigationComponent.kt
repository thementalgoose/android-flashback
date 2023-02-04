package tmg.flashback.debug.noop

import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.model.DebugMenuItem
import javax.inject.Inject

internal class NoopDebugNavigationComponent @Inject constructor(): DebugNavigationComponent {
    override fun navigateTo(id: String) { /* no-op */ }
    override fun getDebugMenuItems(): List<DebugMenuItem> = emptyList()
}