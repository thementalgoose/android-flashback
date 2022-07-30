package tmg.flashback.debug

import android.content.Intent
import tmg.flashback.debug.adverts.AdvertsActivity
import tmg.flashback.debug.styleguide.StyleGuideComposeActivity
import tmg.flashback.ui.navigation.ActivityProvider
import javax.inject.Inject

class DebugNavigationComponent @Inject constructor(
    private val activityProvider: ActivityProvider
) {
    fun navigateTo(id: String) {
        when (id) {
            MENU_STYLEGUIDE -> activityProvider.launch {
                it.startActivity(Intent(it, StyleGuideComposeActivity::class.java))
            }
            MENU_DEBUG -> activityProvider.launch {
                it.startActivity(Intent(it, DebugActivity::class.java))
            }
            MENU_ADVERTS -> activityProvider.launch {
                it.startActivity(Intent(it, AdvertsActivity::class.java))
            }
            else -> {

            }
        }
    }
}