package tmg.flashback.rss

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

class RssNavigationComponent @Inject constructor(
    private val navigator: Navigator
) {
    fun rss() {
        navigator.navigate(Screen.RSS)
    }

    fun configureRSS() {
        navigator.navigate(Screen.Settings.RSSConfigure)
    }
}