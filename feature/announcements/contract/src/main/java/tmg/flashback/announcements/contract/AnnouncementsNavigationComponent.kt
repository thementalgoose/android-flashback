package tmg.flashback.announcements.contract

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

val Screen.Announcements: NavigationDestination
    get() = NavigationDestination("announcements", launchSingleTop = true)