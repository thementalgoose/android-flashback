package tmg.flashback.debug

import android.content.Intent
import tmg.flashback.ui.navigation.ActivityProvider

class DebugNavigationComponent(
    private val activityProvider: ActivityProvider
) {
    fun navigateTo(id: String) {
        when (id) {
            MENU_DEBUG -> activityProvider.launch {
                it.startActivity(Intent(it, DebugActivity::class.java))
            }
            else -> {

            }
        }
    }
}