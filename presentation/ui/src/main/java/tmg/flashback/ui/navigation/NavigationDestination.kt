package tmg.flashback.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

data class NavigationDestination(
    val route: String,
    val launchSingleTop: Boolean = false,
)

fun NavController.navigate(destination: NavigationDestination, builder: NavOptionsBuilder.() -> Unit = {
    this.launchSingleTop = launchSingleTop
}) {
    this.navigate(route = destination.route, builder = builder)
}

fun String.asNavigationDestination() = NavigationDestination(this)