package tmg.flashback.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

interface NavigationDestination {
    val route: String
}

fun NavController.navigate(destination: NavigationDestination, builder: NavOptionsBuilder.() -> Unit = { }) {
    this.navigate(route = destination.route, builder = builder)
}