package tmg.flashback.rss.contract

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

val Screen.Settings.RSS: NavigationDestination
    get() = NavigationDestination("settings/rss", popUpTo = "settings")

val Screen.Settings.RSSConfigure: NavigationDestination
    get() = NavigationDestination(
        "settings/rss/configure",
        popUpTo = "settings/rss"
    )

val Screen.RSS: NavigationDestination
    get() = NavigationDestination("rss", launchSingleTop = true)