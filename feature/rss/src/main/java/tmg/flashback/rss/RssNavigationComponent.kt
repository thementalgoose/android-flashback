package tmg.flashback.rss

import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import javax.inject.Inject

val Screen.Settings.RSS: NavigationDestination
    get() = NavigationDestination("settings/rss", popUpTo = "settings")

val Screen.Settings.RSSConfigure: NavigationDestination
    get() = NavigationDestination("settings/rss/configure", popUpTo = "settings/rss")

val Screen.RSS: NavigationDestination
    get() = NavigationDestination("rss", launchSingleTop = true)

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