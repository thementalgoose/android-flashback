package tmg.flashback.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

@JvmInline
value class NavigationDestination(
    val route: String
)

fun NavController.navigate(destination: NavigationDestination, builder: NavOptionsBuilder.() -> Unit = { }) {
    this.navigate(route = destination.route, builder = builder)
}

fun String.asNavigationDestination() = NavigationDestination(this)