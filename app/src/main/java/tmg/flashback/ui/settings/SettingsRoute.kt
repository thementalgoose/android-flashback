package tmg.flashback.ui.settings

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

internal val Screen.Settings.All: NavigationDestination
    get() = NavigationDestination("settings", launchSingleTop = true)