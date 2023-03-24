package tmg.flashback.ui.settings.appearance

import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.navigation.ActivityProvider
import tmg.flashback.ui.settings.appearance.nightmode.NightModeBottomSheetFragment
import tmg.flashback.ui.settings.appearance.theme.ThemeBottomSheetFragment
import javax.inject.Inject

class AppearanceNavigationComponent @Inject constructor(
    private val activityProvider: tmg.flashback.navigation.ActivityProvider
) {
    fun nightModeDialog() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        NightModeBottomSheetFragment().show(activity.supportFragmentManager, "NIGHT_MODE")
    }

    fun themeDialog() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        ThemeBottomSheetFragment().show(activity.supportFragmentManager, "THEME")
    }
}