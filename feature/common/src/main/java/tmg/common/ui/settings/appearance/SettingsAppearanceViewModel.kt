package tmg.common.ui.settings.appearance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.components.prefs.AppPreferencesItem
import tmg.core.ui.R
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsAppearanceViewModelInputs {
    fun preferenceClicked(pref: String?, value: Boolean?)
}

//endregion

//region Outputs

interface SettingsAppearanceViewModelOutputs {
    val settings: LiveData<List<AppPreferencesItem>>

    val openTheme: LiveData<Event>
    val openAnimationSpeed: LiveData<Event>
}

//endregion

class SettingsAppearanceViewModel: ViewModel(), SettingsAppearanceViewModelInputs,
    SettingsAppearanceViewModelOutputs {

    private val keyTheme: String = "Theme"
    private val keyAnimationSpeed: String = "AnimationSpeed"

    var inputs: SettingsAppearanceViewModelInputs = this
    var outputs: SettingsAppearanceViewModelOutputs = this

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()
    override val openTheme: MutableLiveData<Event> = MutableLiveData()
    override val openAnimationSpeed: MutableLiveData<Event> = MutableLiveData()

    init {
        settings.value = mutableListOf<AppPreferencesItem>().apply {
            add(AppPreferencesItem.Category(R.string.settings_theme_title))
            add(AppPreferencesItem.Preference(keyTheme, R.string.settings_theme_theme_title, R.string.settings_theme_theme_description))
            add(AppPreferencesItem.Preference(keyAnimationSpeed, R.string.settings_theme_animation_speed_title, R.string.settings_theme_animation_speed_description))
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
