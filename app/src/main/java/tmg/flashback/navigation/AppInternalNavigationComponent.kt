package tmg.flashback.navigation

import kotlinx.serialization.json.Json
import tmg.flashback.circuits.navigation.ScreenCircuitData
import tmg.flashback.constructors.navigation.ScreenConstructorData
import tmg.flashback.drivers.navigation.ScreenDriverData
import javax.inject.Inject

class AppInternalNavigationComponent @Inject constructor(): InternalNavigationComponent {
    override fun getNavigationData(screen: Screen): NavigationDestination {
        return when (screen) {
            is Screen.Circuit -> {
                val data = ScreenCircuitData(screen.circuitId, screen.circuitName)
                NavigationDestination(
                    route = screen.route.replace("{data}", Json.encodeToString(ScreenCircuitData.serializer(), data)),
                )
            }
            is Screen.Constructor -> {
                val data = ScreenConstructorData(screen.constructorId, screen.constructorName)
                NavigationDestination(
                    route = screen.route.replace("{data}", Json.encodeToString(ScreenConstructorData.serializer(), data))
                )
            }
            is Screen.Driver -> {
                val data = ScreenDriverData(screen.driverId, screen.driverName)
                NavigationDestination(
                    route = screen.route.replace("{data}", Json.encodeToString(ScreenDriverData.serializer(), data))
                )
            }
            Screen.ConstructorStandings -> NavigationDestination(
                route = screen.route,
                launchSingleTop = true,
                popUpTo = Screen.Races.route
            )
            Screen.DriverStandings -> NavigationDestination(
                route = screen.route,
                launchSingleTop = true,
                popUpTo = Screen.Races.route
            )
            Screen.PrivacyPolicy -> NavigationDestination(
                route = screen.route
            )
            Screen.Races -> NavigationDestination(
                route = screen.route,
                launchSingleTop = true
            )
            Screen.Rss -> NavigationDestination(
                route = screen.route,
                launchSingleTop = true,
                popUpTo = Screen.Races.route
            )
            Screen.Settings -> NavigationDestination(
                route = screen.route,
                launchSingleTop = true,
                popUpTo = Screen.Races.route
            )
            Screen.ReactionGame -> NavigationDestination(
                route = screen.route,
                launchSingleTop = true
            )
            Screen.Search -> NavigationDestination(
                route = screen.route,
                launchSingleTop = true,
                popUpTo = Screen.Races.route
            )
        }
    }
}