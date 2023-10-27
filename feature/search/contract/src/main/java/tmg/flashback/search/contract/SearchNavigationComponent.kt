package tmg.flashback.search.contract

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

val Screen.Search: NavigationDestination
    get() = NavigationDestination("search", launchSingleTop = true, popUpTo = "results/races")