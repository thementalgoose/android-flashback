package tmg.flashback.debug.core

import android.content.Intent
import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.core.adverts.AdvertsActivity
import tmg.flashback.debug.core.styleguide.StyleGuideComposeActivity
import tmg.flashback.debug.model.DebugMenuItem
import tmg.flashback.navigation.ActivityProvider
import tmg.flashback.navigation.ApplicationNavigationComponent
import javax.inject.Inject

internal class DebugNavigationComponentImpl @Inject constructor(
    private val activityProvider: ActivityProvider,
    private val navComponent: ApplicationNavigationComponent
): DebugNavigationComponent {
    override fun navigateTo(id: String) {
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
            MENU_SYNC -> activityProvider.launch {
                navComponent.syncActivity()
            }
            else -> { }
        }
    }

    override fun getDebugMenuItems(): List<DebugMenuItem> = listOf(
        DebugMenuItem(R.string.debug_menu_debug, R.drawable.debug_list_debug, MENU_DEBUG),
        DebugMenuItem(R.string.debug_menu_styleguide, R.drawable.debug_list_styleguide, MENU_STYLEGUIDE),
        DebugMenuItem(R.string.debug_menu_ads_config, R.drawable.debug_list_adverts, MENU_ADVERTS),
        DebugMenuItem(R.string.debug_menu_sync, R.drawable.debug_list_sync, MENU_SYNC)
    )

    companion object {
        internal const val MENU_DEBUG = "debug"
        internal const val MENU_STYLEGUIDE = "styleguide"
        internal const val MENU_ADVERTS = "adverts"
        internal const val MENU_SYNC = "sync"
    }
}