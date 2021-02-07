package tmg.flashback.ui.settings.customisation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.AppPreferencesItem
import tmg.components.prefs.AppPreferencesItem.Category
import tmg.components.prefs.AppPreferencesItem.Preference
import tmg.flashback.R
import tmg.flashback.core.controllers.AppearanceController
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.core.enums.Theme
import tmg.flashback.core.ui.BaseViewModel
import tmg.flashback.extensions.icon
import tmg.flashback.extensions.label
import tmg.flashback.ui.utils.Selected
import tmg.flashback.ui.utils.StringHolder
import tmg.flashback.ui.utils.bottomsheet.BottomSheetItem
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsCustomisationViewModelInputs {
    fun preferenceClicked(pref: String?, value: Boolean?)

    fun selectTheme(theme: Theme)
    fun selectAnimationSpeed(animationSpeed: AnimationSpeed)
}

//endregion

//region Outputs

interface SettingsCustomisationViewModelOutputs {
    val settings: LiveData<List<AppPreferencesItem>>

    val themePreferences: LiveData<List<Selected<BottomSheetItem>>>
    val themeOpenPicker: LiveData<Event>
    val themeUpdated: LiveData<Event>

    val animationSpeedPreference: LiveData<List<Selected<BottomSheetItem>>>
    val animationSpeedOpenPicker: LiveData<Event>
    val animationSpeedUpdated: LiveData<Event>
}

//endregion

class SettingsCustomisationViewModel(
    private val appearanceController: AppearanceController
): BaseViewModel(), SettingsCustomisationViewModelInputs, SettingsCustomisationViewModelOutputs {

    private val keyTheme: String = "Theme"
    private val keyAnimationSpeed: String = "AnimationSpeed"

    var inputs: SettingsCustomisationViewModelInputs = this
    var outputs: SettingsCustomisationViewModelOutputs = this

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()

    override val themePreferences: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()
    override val themeOpenPicker: MutableLiveData<Event> = MutableLiveData()
    override val themeUpdated: MutableLiveData<Event> = MutableLiveData()

    override val animationSpeedPreference: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()
    override val animationSpeedOpenPicker: MutableLiveData<Event> = MutableLiveData()
    override val animationSpeedUpdated: MutableLiveData<Event> = MutableLiveData()

    init {
        settings.value = mutableListOf<AppPreferencesItem>().apply {
            add(Category(R.string.settings_customisation))
            add(Preference(keyTheme, R.string.settings_theme_theme_title, R.string.settings_theme_theme_description))
            add(Preference(keyAnimationSpeed, R.string.settings_animation_speed_animation_title, R.string.settings_animation_speed_animation_description))
        }

        updateThemeList()
        updateAnimationList()
    }

    //region Inputs

    override fun preferenceClicked(pref: String?, value: Boolean?) {
        when (pref) {
            keyTheme -> themeOpenPicker.value = Event()
            keyAnimationSpeed -> animationSpeedOpenPicker.value = Event()
        }
    }

    override fun selectTheme(theme: Theme) {
        appearanceController.currentTheme = theme
        updateThemeList()
        themeUpdated.value = Event()
    }

    override fun selectAnimationSpeed(animationSpeed: AnimationSpeed) {
        appearanceController.animationSpeed = animationSpeed
        updateAnimationList()
        animationSpeedUpdated.value = Event()
    }

    //endregion

    private fun updateThemeList() {
        themePreferences.value = Theme.values()
            .map {
                Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == appearanceController.currentTheme)
            }
    }

    private fun updateAnimationList() {
        animationSpeedPreference.value = AnimationSpeed.values()
            .map {
                Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == appearanceController.animationSpeed)
            }
    }
}
