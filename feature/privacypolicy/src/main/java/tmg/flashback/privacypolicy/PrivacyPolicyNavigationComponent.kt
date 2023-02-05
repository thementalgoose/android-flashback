package tmg.flashback.privacypolicy

import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import javax.inject.Inject

val Screen.Settings.PrivacyPolicy: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "privacy_policy"
    }

class PrivacyPolicyNavigationComponent @Inject constructor(
    private val navigator: Navigator,
    private val activityProvider: ActivityProvider
) {

    fun privacyPolicy() = activityProvider.launch {
        navigator.navigate(Screen.Settings.PrivacyPolicy)
    }
}