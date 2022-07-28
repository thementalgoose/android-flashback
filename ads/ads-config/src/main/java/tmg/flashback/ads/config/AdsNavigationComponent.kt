package tmg.flashback.ads.config

import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen

val Screen.Settings.Adverts: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/adverts"
    }

class AdsNavigationComponent(
    private val navigator: Navigator
) {
    fun settingsAds() {
        navigator.navigate(Screen.Settings.Adverts)
    }
}