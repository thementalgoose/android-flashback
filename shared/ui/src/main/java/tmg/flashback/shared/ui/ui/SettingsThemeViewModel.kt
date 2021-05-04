package tmg.flashback.shared.ui.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.shared.ui.R
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsThemeViewModelInputs {
    fun preferenceClicked(pref: String?, value: Boolean?)
}

//endregion

//region Outputs

interface SettingsThemeViewModelOutputs {
    val settings: LiveData<List<AppPreferencesItem>>

    val openTheme: LiveData<Event>
    val openAnimationSpeed: LiveData<Event>
}

//endregion

class SettingsThemeViewModel: ViewModel(), SettingsThemeViewModelInputs, SettingsThemeViewModelOutputs {

    private val keyTheme: String = "Theme"
    private val keyAnimationSpeed: String = "AnimationSpeed"

    var inputs: SettingsThemeViewModelInputs = this
    var outputs: SettingsThemeViewModelOutputs = this

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
