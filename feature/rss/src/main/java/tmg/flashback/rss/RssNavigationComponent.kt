package tmg.flashback.rss

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import tmg.flashback.rss.ui.configure.SettingsRSSConfigureScreenVM
import tmg.flashback.rss.ui.feed.RSSScreenVM
import tmg.flashback.rss.ui.settings.SettingsRSSScreenVM
import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import javax.inject.Inject

val Screen.Settings.RSS: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/rss"
    }

val Screen.Settings.RSSConfigure: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/rss/configure"
    }

val Screen.RSS: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "rss"
    }

fun NavGraphBuilder.rss(navController: NavController) {
    composable(
        Screen.RSS.route,
        deepLinks = listOf(navDeepLink { uriPattern = "flashback://rss" })
    ) {
        RSSScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }

    composable(Screen.Settings.RSSConfigure.route) {
        SettingsRSSScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }

    composable(Screen.Settings.RSSConfigure.route) {
        SettingsRSSConfigureScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }
}

class RssNavigationComponent @Inject constructor(
    private val navigator: Navigator
) {
    fun rss() {
        navigator.navigate(Screen.RSS)
    }

    fun settingsRSS() {
        navigator.navigate(Screen.Settings.RSS)
    }

    fun configureRSS() {
        navigator.navigate(Screen.Settings.RSSConfigure)
    }
}