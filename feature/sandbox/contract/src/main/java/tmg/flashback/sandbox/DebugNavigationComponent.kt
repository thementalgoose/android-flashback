package tmg.flashback.sandbox

import tmg.flashback.sandbox.model.DebugMenuItem

interface DebugNavigationComponent {
    fun navigateTo(id: String)
    fun getDebugMenuItems(): List<DebugMenuItem>
}