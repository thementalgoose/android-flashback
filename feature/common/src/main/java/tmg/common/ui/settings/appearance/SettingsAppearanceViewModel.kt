package tmg.common.ui.settings.appearance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.core.ui.settings.SettingsModel
import tmg.core.ui.settings.SettingsViewModel
import tmg.core.ui.R
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsAppearanceViewModelInputs {
}

//endregion

//region Outputs

interface SettingsAppearanceViewModelOutputs {
    val openTheme: LiveData<Event>
    val openAnimationSpeed: LiveData<Event>
}

//endregion

class SettingsAppearanceViewModel: SettingsViewModel(), SettingsAppearanceViewModelInputs,
    SettingsAppearanceViewModelOutputs {

    override val models: List<SettingsModel> = listOf(
        SettingsModel.Header(R.string.settings_theme_title),
        SettingsModel.Pref(
            title = R.string.settings_theme_theme_title,
            description = R.string.settings_theme_theme_description,
            onClick = {
                openTheme.value = Event()
            }
        ),
        SettingsModel.Pref(
            title = R.string.settings_theme_animation_speed_title,
            description = R.string.settings_theme_animation_speed_description,
            onClick = {
                openAnimationSpeed.value = Event()
            }
        ),
    )

    var inputs: SettingsAppearanceViewModelInputs = this
    var outputs: SettingsAppearanceViewModelOutputs = this

    override val openTheme: MutableLiveData<Event> = MutableLiveData()
    override val openAnimationSpeed: MutableLiveData<Event> = MutableLiveData()

}
