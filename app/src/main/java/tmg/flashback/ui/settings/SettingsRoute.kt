package tmg.flashback.ui.settings

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

internal val tmg.flashback.navigation.Screen.Settings.All: tmg.flashback.navigation.NavigationDestination
    get() = tmg.flashback.navigation.NavigationDestination("settings", launchSingleTop = true)

internal val tmg.flashback.navigation.Screen.Settings.Adverts: tmg.flashback.navigation.NavigationDestination
    get() = tmg.flashback.navigation.NavigationDestination("settings/adverts", popUpTo = "settings")

internal val tmg.flashback.navigation.Screen.Settings.Home: tmg.flashback.navigation.NavigationDestination
    get() = tmg.flashback.navigation.NavigationDestination("settings/home", popUpTo = "settings")


internal val tmg.flashback.navigation.Screen.Settings.Web: tmg.flashback.navigation.NavigationDestination
    get() = tmg.flashback.navigation.NavigationDestination("settings/web", popUpTo = "settings")


internal val tmg.flashback.navigation.Screen.Settings.NotificationsUpcoming: tmg.flashback.navigation.NavigationDestination
    get() = tmg.flashback.navigation.NavigationDestination(
        "settings/notifications/upcoming",
        popUpTo = "settings"
    )

internal val tmg.flashback.navigation.Screen.Settings.NotificationsResults: tmg.flashback.navigation.NavigationDestination
    get() = tmg.flashback.navigation.NavigationDestination(
        "settings/notifications/results",
        popUpTo = "settings"
    )


internal val tmg.flashback.navigation.Screen.Settings.Ads: tmg.flashback.navigation.NavigationDestination
    get() = tmg.flashback.navigation.NavigationDestination("settings/ads", popUpTo = "settings")

internal val tmg.flashback.navigation.Screen.Settings.Privacy: tmg.flashback.navigation.NavigationDestination
    get() = tmg.flashback.navigation.NavigationDestination("settings/privacy", popUpTo = "settings")

internal val tmg.flashback.navigation.Screen.Settings.About: tmg.flashback.navigation.NavigationDestination
    get() = tmg.flashback.navigation.NavigationDestination("settings/about", popUpTo = "settings")