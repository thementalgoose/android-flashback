package tmg.flashback.sandbox.noop

import tmg.flashback.sandbox.SandboxNavigationComponent
import tmg.flashback.sandbox.model.SandboxMenuItem
import javax.inject.Inject

internal class NoopSandboxNavigationComponent @Inject constructor(): SandboxNavigationComponent {
    override fun navigateTo(id: String) { /* no-op */ }
    override fun getDebugMenuItems(): List<SandboxMenuItem> = emptyList()
}