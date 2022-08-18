package tmg.flashback.settings.ui.settings.appearance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.settings.SettingsNavigationComponent
import tmg.flashback.ui.R
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel
import tmg.utilities.lifecycle.Event
import javax.inject.Inject

//region Inputs

interface SettingsAppearanceViewModelInputs {
}

//endregion

//region Outputs

interface SettingsAppearanceViewModelOutputs {
}

//endregion

@HiltViewModel
class SettingsAppearanceViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
    private val buildConfig: BuildConfigManager,
    private val settingsNavigationComponent: SettingsNavigationComponent
): SettingsViewModel(), SettingsAppearanceViewModelInputs,
    SettingsAppearanceViewModelOutputs {

    override val models: List<SettingsModel> = mutableListOf<SettingsModel>().apply {
        add(SettingsModel.Header(R.string.settings_theme_title))

        if (themeRepository.enableThemePicker && buildConfig.isMonetThemeSupported) {
            add(SettingsModel.Pref(
                title = R.string.settings_theme_theme_title,
                description = R.string.settings_theme_theme_description,
                onClick = {
                    settingsNavigationComponent.themeDialog()
                }
            ))
        }

        add(SettingsModel.Pref(
            title = R.string.settings_theme_nightmode_title,
            description = R.string.settings_theme_nightmode_description,
            onClick = {
                settingsNavigationComponent.nightModeDialog()
            }
        ))
        add(SettingsModel.Pref(
            title = R.string.settings_theme_animation_speed_title,
            description = R.string.settings_theme_animation_speed_description,
            onClick = {
                settingsNavigationComponent.animationDialog()
            }
        ))
    }

    var inputs: SettingsAppearanceViewModelInputs = this
    var outputs: SettingsAppearanceViewModelOutputs = this
}
