package tmg.flashback.settings

import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.settings.ui.settings.appearance.animation.AnimationSpeedBottomSheetFragment
import tmg.flashback.settings.ui.settings.appearance.nightmode.NightModeBottomSheetFragment
import tmg.flashback.settings.ui.settings.appearance.theme.ThemeBottomSheetFragment
import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import javax.inject.Inject

val Screen.Settings.About: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/about"
    }

val Screen.Settings.Appearance: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/appearance"
    }

val Screen.Settings.Support: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/support"
    }

val Screen.Settings.PrivacyPolicy: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "privacy_policy"
    }

class SettingsNavigationComponent @Inject constructor(
    private val navigator: Navigator,
    private val activityProvider: ActivityProvider
) {
    fun animationDialog() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        AnimationSpeedBottomSheetFragment().show(activity.supportFragmentManager, "ANIMATION")
    }

    fun nightModeDialog() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        NightModeBottomSheetFragment().show(activity.supportFragmentManager, "NIGHT_MODE")
    }

    fun themeDialog() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        ThemeBottomSheetFragment().show(activity.supportFragmentManager, "THEME")
    }

    fun settingsAbout() {
        navigator.navigate(Screen.Settings.About)
    }

    fun settingsAppearance() {
        navigator.navigate(Screen.Settings.Appearance)
    }

    fun settingsSupport() {
        navigator.navigate(Screen.Settings.Support)
    }

    fun privacyPolicy() {
        navigator.navigate(Screen.Settings.PrivacyPolicy)
    }
}