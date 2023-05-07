package tmg.flashback.privacypolicy.contract

import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

val Screen.Settings.PrivacyPolicy: NavigationDestination
    get() = NavigationDestination("privacy_policy")
