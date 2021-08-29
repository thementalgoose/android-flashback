package tmg.common.ui.settings.appearance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.core.device.managers.BuildConfigManager
import tmg.core.ui.settings.SettingsModel
import tmg.core.ui.settings.SettingsViewModel
import tmg.core.ui.R
import tmg.core.ui.controllers.ThemeController
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsAppearanceViewModelInputs {
}

//endregion

//region Outputs

interface SettingsAppearanceViewModelOutputs {
    val openTheme: LiveData<Event>
    val openNightMode: LiveData<Event>
    val openAnimationSpeed: LiveData<Event>
}

//endregion

class SettingsAppearanceViewModel(
    private val themeController: ThemeController,
    private val buildConfig: BuildConfigManager
): SettingsViewModel(), SettingsAppearanceViewModelInputs,
    SettingsAppearanceViewModelOutputs {

    override val models: List<SettingsModel> = mutableListOf<SettingsModel>().apply {
        add(SettingsModel.Header(R.string.settings_theme_title))

        if (themeController.enableThemePicker && buildConfig.isMonetThemeSupported) {
            add(SettingsModel.Pref(
                title = R.string.settings_theme_theme_title,
                description = R.string.settings_theme_theme_description,
                onClick = {
                    openTheme.value = Event()
                }
            ))
        }

        add(SettingsModel.Pref(
            title = R.string.settings_theme_nightmode_title,
            description = R.string.settings_theme_nightmode_description,
            onClick = {
                openNightMode.value = Event()
            }
        ))
        add(SettingsModel.Pref(
            title = R.string.settings_theme_animation_speed_title,
            description = R.string.settings_theme_animation_speed_description,
            onClick = {
                openAnimationSpeed.value = Event()
            }
        ))
    }

    var inputs: SettingsAppearanceViewModelInputs = this
    var outputs: SettingsAppearanceViewModelOutputs = this

    override val openTheme: MutableLiveData<Event> = MutableLiveData()
    override val openNightMode: MutableLiveData<Event> = MutableLiveData()
    override val openAnimationSpeed: MutableLiveData<Event> = MutableLiveData()

}
