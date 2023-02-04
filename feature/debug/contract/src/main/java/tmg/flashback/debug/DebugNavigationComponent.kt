package tmg.flashback.debug

import tmg.flashback.debug.model.DebugMenuItem

interface DebugNavigationComponent {
    fun navigateTo(id: String)
    fun getDebugMenuItems(): List<DebugMenuItem>
}