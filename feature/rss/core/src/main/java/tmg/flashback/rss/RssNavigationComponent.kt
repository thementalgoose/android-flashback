package tmg.flashback.rss

import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Screen

val Screen.Settings.RSS: NavigationDestination
    get() = NavigationDestination("settings/rss", popUpTo = "settings")

val Screen.Settings.RSSConfigure: NavigationDestination
    get() = NavigationDestination("settings/rss/configure", popUpTo = "settings/rss")

val Screen.RSS: NavigationDestination
    get() = NavigationDestination("rss", launchSingleTop = true)