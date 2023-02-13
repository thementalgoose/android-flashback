package tmg.flashback.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptionsBuilder

data class NavigationDestination(
    val route: String,
    val launchSingleTop: Boolean = false,
    val popUpTo: String? = null,
)

fun NavController.navigate(destination: NavigationDestination, builder: NavOptionsBuilder.() -> Unit = {
    this.launchSingleTop = destination.launchSingleTop
    if (destination.launchSingleTop || destination.popUpTo != null) {
        if (destination.popUpTo != null) {
            popUpTo(destination.popUpTo) {
                saveState = true
            }
        } else {
            popUpTo(this@navigate.graph.startDestinationId) {
                saveState = true
            }
        }
    }
}) {
    this.navigate(route = destination.route, builder = builder)
}

fun String.asNavigationDestination() = NavigationDestination(this)