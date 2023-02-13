package tmg.flashback.ui.settings

import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Screen

internal val Screen.Settings.All: NavigationDestination
    get() = NavigationDestination("settings", launchSingleTop = true)

internal val Screen.Settings.Adverts: NavigationDestination
    get() = NavigationDestination("settings/adverts")

internal val Screen.Settings.Home: NavigationDestination
    get() = NavigationDestination("settings/home")


internal val Screen.Settings.Web: NavigationDestination
    get() = NavigationDestination("settings/web")


internal val Screen.Settings.NotificationsUpcoming: NavigationDestination
    get() = NavigationDestination("settings/notifications/upcoming")

internal val Screen.Settings.NotificationsResults: NavigationDestination
    get() = NavigationDestination("settings/notifications/results")


internal val Screen.Settings.Ads: NavigationDestination
    get() = NavigationDestination("settings/ads")

internal val Screen.Settings.Privacy: NavigationDestination
    get() = NavigationDestination("settings/privacy")

internal val Screen.Settings.About: NavigationDestination
    get() = NavigationDestination("settings/about")