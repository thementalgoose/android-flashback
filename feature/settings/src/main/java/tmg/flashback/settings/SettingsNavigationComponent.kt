package tmg.flashback.settings

import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.settings.ui.settings.appearance.animation.AnimationSpeedBottomSheetFragment
import tmg.flashback.settings.ui.settings.appearance.nightmode.NightModeBottomSheetFragment
import tmg.flashback.settings.ui.settings.appearance.theme.ThemeBottomSheetFragment
import tmg.flashback.ui.navigation.ActivityProvider

class SettingsNavigationComponent(
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
}