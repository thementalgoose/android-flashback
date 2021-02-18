package tmg.flashback.ui.settings.customisation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.AppPreferencesItem
import tmg.components.prefs.AppPreferencesItem.Category
import tmg.components.prefs.AppPreferencesItem.Preference
import tmg.flashback.R
import tmg.flashback.core.ui.BaseViewModel
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsCustomisationViewModelInputs {
    fun preferenceClicked(pref: String?, value: Boolean?)

}

//endregion

//region Outputs

interface SettingsCustomisationViewModelOutputs {
    val settings: LiveData<List<AppPreferencesItem>>

    val openTheme: LiveData<Event>
    val openAnimationSpeed: LiveData<Event>
}

//endregion

class SettingsCustomisationViewModel: BaseViewModel(), SettingsCustomisationViewModelInputs, SettingsCustomisationViewModelOutputs {

    private val keyTheme: String = "Theme"
    private val keyAnimationSpeed: String = "AnimationSpeed"

    var inputs: SettingsCustomisationViewModelInputs = this
    var outputs: SettingsCustomisationViewModelOutputs = this

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()
    override val openTheme: MutableLiveData<Event> = MutableLiveData()
    override val openAnimationSpeed: MutableLiveData<Event> = MutableLiveData()

    init {
        settings.value = mutableListOf<AppPreferencesItem>().apply {
            add(Category(R.string.settings_customisation))
            add(Preference(keyTheme, R.string.settings_theme_theme_title, R.string.settings_theme_theme_description))
            add(Preference(keyAnimationSpeed, R.string.settings_animation_speed_animation_title, R.string.settings_animation_speed_animation_description))
        }
    }

    //region Inputs

    override fun preferenceClicked(pref: String?, value: Boolean?) {
        when (pref) {
            keyTheme -> openTheme.value = Event()
            keyAnimationSpeed -> openAnimationSpeed.value = Event()
        }
    }

    //endregion
}
