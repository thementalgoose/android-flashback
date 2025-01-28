package tmg.flashback.sandbox

import tmg.flashback.sandbox.model.SandboxMenuItem

interface SandboxNavigationComponent {
    fun navigateTo(id: String)
    fun getDebugMenuItems(): List<SandboxMenuItem>
}