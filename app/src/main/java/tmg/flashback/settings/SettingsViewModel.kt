package tmg.flashback.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.base.BaseViewModel
import tmg.flashback.extensions.icon
import tmg.flashback.extensions.label
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.utils.Selected
import tmg.flashback.utils.bottomsheet.BottomSheetItem
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsViewModelInputs {
    fun preferenceClicked(pref: SettingsOptions?, value: Boolean?)
    fun pickTheme(theme: ThemePref)
}

//endregion

//region Outputs

interface SettingsViewModelOutputs {
    val settings: MutableLiveData<List<AppPreferencesItem>>

    val themePreferences: MutableLiveData<List<Selected<BottomSheetItem>>>

    val themeChanged: MutableLiveData<Event>
    val openThemePicker: MutableLiveData<Event>
    val openAbout: MutableLiveData<Event>
    val openRelease: MutableLiveData<Event>
    val openSuggestions: MutableLiveData<Event>
}

//endregion

class SettingsViewModel(
    private val prefDB: PrefsDB,
    applicationContext: Context
): BaseViewModel(), SettingsViewModelInputs, SettingsViewModelOutputs {

    var inputs: SettingsViewModelInputs = this
    var outputs: SettingsViewModelOutputs = this

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()

    override val themeChanged: MutableLiveData<Event> = MutableLiveData()

    override val openThemePicker: MutableLiveData<Event> = MutableLiveData()
    override val openAbout: MutableLiveData<Event> = MutableLiveData()
    override val openRelease: MutableLiveData<Event> = MutableLiveData()
    override val openSuggestions: MutableLiveData<Event> = MutableLiveData()

    override val themePreferences: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()

    init {
        settings.value = listOf(
//            AppPreferencesItem.Category(applicationContext.getString(R.string.settings_theme)),
//            SettingsOptions.THEME.toPref(applicationContext),
            AppPreferencesItem.Category(applicationContext.getString(R.string.settings_customisation)),
            SettingsOptions.QUALIFYING_DELTAS.toSwitch(applicationContext, prefDB.showQualifyingDelta),
            SettingsOptions.SHOW_DRIVERS_POINTS_IN_CONSTRUCTORS.toSwitch(applicationContext, prefDB.showDriversBehindConstructor),
            AppPreferencesItem.Category(applicationContext.getString(R.string.settings_help)),
            SettingsOptions.ABOUT.toPref(applicationContext),
            SettingsOptions.RELEASE.toPref(applicationContext),
            AppPreferencesItem.Category(applicationContext.getString(R.string.settings_feedback)),
            SettingsOptions.CRASH.toSwitch(applicationContext, prefDB.crashReporting),
            SettingsOptions.SUGGESTION.toPref(applicationContext),
            SettingsOptions.SHAKE.toSwitch(applicationContext, prefDB.shakeToReport)
        )

        updateThemeList()
    }

    //region Inputs

    override fun preferenceClicked(pref: SettingsOptions?, value: Boolean?) {
        when (pref) {
            SettingsOptions.THEME -> openThemePicker.value = Event()
            SettingsOptions.QUALIFYING_DELTAS -> prefDB.showQualifyingDelta = value ?: false
            SettingsOptions.SHOW_DRIVERS_POINTS_IN_CONSTRUCTORS -> prefDB.showDriversBehindConstructor = value ?: true
            SettingsOptions.ABOUT -> openAbout.value = Event()
            SettingsOptions.RELEASE -> openRelease.value = Event()
            SettingsOptions.CRASH -> prefDB.crashReporting = value ?: true
            SettingsOptions.SUGGESTION -> openSuggestions.value = Event()
            SettingsOptions.SHAKE -> prefDB.shakeToReport = value ?: true
        }
    }

    override fun pickTheme(theme: ThemePref) {
        prefDB.theme = theme
        updateThemeList()
        themeChanged.value = Event()
    }

    //endregion

    private fun updateThemeList() {
        themePreferences.value = ThemePref.values()
            .map {
                Selected(BottomSheetItem(it.ordinal, it.icon, it.label), it == prefDB.theme)
            }
    }

}