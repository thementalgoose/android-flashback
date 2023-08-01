package tmg.flashback.ui.settings

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

internal val Screen.Settings.All: NavigationDestination
    get() = NavigationDestination("settings", launchSingleTop = true)

internal val Screen.Settings.NightMode: NavigationDestination
    get() = NavigationDestination("settings/nightmode", popUpTo = "settings")

internal val Screen.Settings.Theme: NavigationDestination
    get() = NavigationDestination("settings/theme", popUpTo = "settings")

internal val Screen.Settings.Adverts: NavigationDestination
    get() = NavigationDestination("settings/adverts", popUpTo = "settings")

internal val Screen.Settings.Home: NavigationDestination
    get() = NavigationDestination("settings/home", popUpTo = "settings")

internal val Screen.Settings.Weather: NavigationDestination
    get() = NavigationDestination("settings/weather", popUpTo = "settings")

internal val Screen.Settings.Web: NavigationDestination
    get() = NavigationDestination("settings/web", popUpTo = "settings")


internal val Screen.Settings.NotificationsUpcoming: NavigationDestination
    get() = NavigationDestination(
        "settings/notifications/upcoming",
        popUpTo = "settings"
    )

internal val Screen.Settings.NotificationsResults: NavigationDestination
    get() = NavigationDestination(
        "settings/notifications/results",
        popUpTo = "settings"
    )


internal val Screen.Settings.Ads: NavigationDestination
    get() = NavigationDestination("settings/ads", popUpTo = "settings")

internal val Screen.Settings.Privacy: NavigationDestination
    get() = NavigationDestination("settings/privacy", popUpTo = "settings")

internal val Screen.Settings.About: NavigationDestination
    get() = NavigationDestination("settings/about", popUpTo = "settings")