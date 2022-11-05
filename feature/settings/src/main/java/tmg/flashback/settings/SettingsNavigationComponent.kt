package tmg.flashback.settings

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import tmg.flashback.releasenotes.ReleaseNotes
import tmg.flashback.releasenotes.ui.releasenotes.ReleaseScreenVM
import tmg.flashback.settings.ui.privacypolicy.PrivacyPolicyScreenVM
import tmg.flashback.settings.ui.settings.appearance.nightmode.NightModeBottomSheetFragment
import tmg.flashback.settings.ui.settings.appearance.theme.ThemeBottomSheetFragment
import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import javax.inject.Inject

val Screen.Settings.PrivacyPolicy: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "privacy_policy"
    }

class SettingsNavigationComponent @Inject constructor(
    private val navigator: Navigator,
    private val activityProvider: ActivityProvider
) {
    fun nightModeDialog() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        NightModeBottomSheetFragment().show(activity.supportFragmentManager, "NIGHT_MODE")
    }

    fun themeDialog() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        ThemeBottomSheetFragment().show(activity.supportFragmentManager, "THEME")
    }

    fun privacyPolicy() = activityProvider.launch {
        navigator.navigate(Screen.Settings.PrivacyPolicy)
    }
}