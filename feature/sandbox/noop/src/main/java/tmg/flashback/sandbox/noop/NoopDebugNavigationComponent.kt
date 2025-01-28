package tmg.flashback.sandbox.noop

import tmg.flashback.sandbox.DebugNavigationComponent
import tmg.flashback.sandbox.model.DebugMenuItem
import javax.inject.Inject

internal class NoopDebugNavigationComponent @Inject constructor(): DebugNavigationComponent {
    override fun navigateTo(id: String) { /* no-op */ }
    override fun getDebugMenuItems(): List<DebugMenuItem> = emptyList()
}