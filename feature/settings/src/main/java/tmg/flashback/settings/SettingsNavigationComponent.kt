package tmg.flashback.settings

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.settings.ui.settings.about.SettingsAboutActivity
import tmg.flashback.settings.ui.settings.appearance.SettingsAppearanceActivity
import tmg.flashback.settings.ui.settings.appearance.animation.AnimationSpeedBottomSheetFragment
import tmg.flashback.settings.ui.settings.appearance.nightmode.NightModeBottomSheetFragment
import tmg.flashback.settings.ui.settings.appearance.theme.ThemeBottomSheetFragment
import tmg.flashback.settings.ui.settings.support.SettingsSupportActivity
import tmg.flashback.ui.navigation.ActivityProvider

class SettingsNavigationComponent(
    private val activityProvider: ActivityProvider
) {
    internal fun animationDialog() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        AnimationSpeedBottomSheetFragment().show(activity.supportFragmentManager, "ANIMATION")
    }

    internal fun nightModeDialog() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        NightModeBottomSheetFragment().show(activity.supportFragmentManager, "NIGHT_MODE")
    }

    internal fun themeDialog() = activityProvider.launch {
        val activity = it as? AppCompatActivity ?: return@launch
        ThemeBottomSheetFragment().show(activity.supportFragmentManager, "THEME")
    }

    internal fun settingsAboutIntent(context: Context): Intent {
        return Intent(context, SettingsAboutActivity::class.java)
    }

    fun settingsAbout() = activityProvider.launch {
        it.startActivity(settingsAboutIntent(it))
    }

    internal fun settingsAppearanceIntent(context: Context): Intent {
        return Intent(context, SettingsAppearanceActivity::class.java)
    }

    fun settingsAppearance() = activityProvider.launch {
        it.startActivity(settingsAppearanceIntent(it))
    }

    internal fun settingsSupportIntent(context: Context): Intent {
        return Intent(context, SettingsSupportActivity::class.java)
    }

    fun settingsSupport() = activityProvider.launch {
        it.startActivity(settingsSupportIntent(it))
    }
}