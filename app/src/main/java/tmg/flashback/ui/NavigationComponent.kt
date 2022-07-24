package tmg.flashback.ui

import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen

val Screen.Home: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "home"
    }

val Screen.Settings.All: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings"
    }