package tmg.flashback.ui.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {

    var destination: MutableStateFlow<NavigationDestination> = MutableStateFlow(object : NavigationDestination {
        override val route: String = "home"
    })

    fun navigate(destination: NavigationDestination) {
        this.destination.value = destination
    }
}