package tmg.flashback.ads.config

import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import javax.inject.Inject

val Screen.Settings.Adverts: NavigationDestination
    get() = NavigationDestination("settings/adverts")