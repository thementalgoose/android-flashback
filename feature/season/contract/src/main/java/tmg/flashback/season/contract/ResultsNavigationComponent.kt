package tmg.flashback.season.contract

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

val Screen.DriverStandings get() = NavigationDestination("results/drivers", launchSingleTop = true, popUpTo = "results/races")

val Screen.ConstructorsStandings get() = NavigationDestination("results/constructors", launchSingleTop = true, popUpTo = "results/races")

val Screen.Races get() = NavigationDestination("results/races", launchSingleTop = true)