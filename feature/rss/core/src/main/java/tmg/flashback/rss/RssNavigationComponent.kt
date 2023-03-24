package tmg.flashback.rss

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

val tmg.flashback.navigation.Screen.Settings.RSS: tmg.flashback.navigation.NavigationDestination
    get() = tmg.flashback.navigation.NavigationDestination("settings/rss", popUpTo = "settings")

val tmg.flashback.navigation.Screen.Settings.RSSConfigure: tmg.flashback.navigation.NavigationDestination
    get() = tmg.flashback.navigation.NavigationDestination(
        "settings/rss/configure",
        popUpTo = "settings/rss"
    )

val tmg.flashback.navigation.Screen.RSS: tmg.flashback.navigation.NavigationDestination
    get() = tmg.flashback.navigation.NavigationDestination("rss", launchSingleTop = true)