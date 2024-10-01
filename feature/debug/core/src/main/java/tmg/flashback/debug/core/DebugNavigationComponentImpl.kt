package tmg.flashback.debug.core

import android.content.Intent
import android.net.Uri
import org.threeten.bp.Year
import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.core.adverts.AdvertsActivity
import tmg.flashback.debug.core.styleguide.StyleGuideComposeActivity
import tmg.flashback.debug.model.DebugMenuItem
import tmg.flashback.device.ActivityProvider
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.reactiongame.contract.ReactionGame
import javax.inject.Inject

internal class DebugNavigationComponentImpl @Inject constructor(
    private val activityProvider: ActivityProvider,
    private val navigator: Navigator,
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
            MENU_REACTIONS -> {
                navigator.navigate(Screen.ReactionGame)
            }
            MENU_F1_RESULTS -> activityProvider.launch {
                val currentYear = Year.now().value
                it.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.formula1.com/en/results.html/${currentYear}/races.html")))
            }
            MENU_F1_DRIVERS -> activityProvider.launch {
                val currentYear = Year.now().value
                it.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.formula1.com/en/results.html/${currentYear}/drivers.html")))
            }
            MENU_F1_CONSTRUCTORS -> activityProvider.launch {
                val currentYear = Year.now().value
                it.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.formula1.com/en/results.html/${currentYear}/team.html")))
            }
            MENU_GITHUB_ACTIONS -> activityProvider.launch {
                it.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/thementalgoose/api-flashback/actions/workflows/sync-formula1.yml")))
            }
            else -> { }
        }
    }

    override fun getDebugMenuItems(): List<DebugMenuItem> = listOf(
        DebugMenuItem(R.string.debug_menu_debug, R.drawable.debug_list_debug, MENU_DEBUG),
        DebugMenuItem(R.string.debug_menu_styleguide, R.drawable.debug_list_styleguide, MENU_STYLEGUIDE),
        DebugMenuItem(R.string.debug_menu_ads_config, R.drawable.debug_list_adverts, MENU_ADVERTS),
        DebugMenuItem(R.string.debug_menu_sync, R.drawable.debug_list_sync, MENU_SYNC),
        DebugMenuItem(tmg.flashback.strings.R.string.reaction_screen_title, R.drawable.debug_list_reactions, MENU_REACTIONS),
        DebugMenuItem(R.string.debug_menu_f1_race, R.drawable.debug_list_formula1, MENU_F1_RESULTS),
        DebugMenuItem(R.string.debug_menu_f1_drivers, R.drawable.debug_list_formula1, MENU_F1_DRIVERS),
        DebugMenuItem(R.string.debug_menu_f1_constructors, R.drawable.debug_list_formula1, MENU_F1_CONSTRUCTORS),
        DebugMenuItem(R.string.debug_menu_github_actions, R.drawable.debug_list_github, MENU_GITHUB_ACTIONS)
    )

    companion object {
        internal const val MENU_DEBUG = "debug"
        internal const val MENU_F1_RESULTS = "f1_race"
        internal const val MENU_F1_DRIVERS = "f1_drivers"
        internal const val MENU_F1_CONSTRUCTORS = "f1_constructors"
        internal const val MENU_GITHUB_ACTIONS = "github_actions"
        internal const val MENU_STYLEGUIDE = "styleguide"
        internal const val MENU_ADVERTS = "adverts"
        internal const val MENU_SYNC = "sync"
        internal const val MENU_REACTIONS = "reactions"
    }
}