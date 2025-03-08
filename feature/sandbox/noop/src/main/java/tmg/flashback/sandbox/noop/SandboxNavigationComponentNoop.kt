package tmg.flashback.sandbox.noop

import tmg.flashback.sandbox.SandboxNavigationComponent
import javax.inject.Inject

internal class SandboxNavigationComponentNoop @Inject constructor(): SandboxNavigationComponent {
    override fun navigateTo(id: String) { /* no-op */ }
}