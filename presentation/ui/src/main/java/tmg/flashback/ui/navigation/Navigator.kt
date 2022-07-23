package tmg.flashback.ui.navigation

import kotlinx.coroutines.flow.MutableStateFlow

class Navigator {

    var destination: MutableStateFlow<NavigationDestination> = MutableStateFlow(object : NavigationDestination {
        override val route: String = "home"
    })

    fun navigate(destination: NavigationDestination) {
        this.destination.value = destination
    }
}