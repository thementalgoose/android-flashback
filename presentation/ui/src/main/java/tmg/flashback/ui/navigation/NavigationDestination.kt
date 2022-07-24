package tmg.flashback.ui.navigation

import androidx.navigation.NavController

interface NavigationDestination {
    val route: String
}

fun NavController.navigate(destination: NavigationDestination) {
    this.navigate(route = destination.route)
}