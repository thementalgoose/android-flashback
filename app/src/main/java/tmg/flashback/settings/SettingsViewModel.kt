package tmg.flashback.settings

import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.base.BaseViewModel
import tmg.flashback.extensions.icon
import tmg.flashback.extensions.label
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.di.async.ScopeProvider
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
    val openNews: MutableLiveData<Event>
}

//endregion

class SettingsViewModel(
    private val prefDB: PrefsDB,
    scopeProvider: ScopeProvider
): BaseViewModel(scopeProvider), SettingsViewModelInputs, SettingsViewModelOutputs {

    var inputs: SettingsViewModelInputs = this
    var outputs: SettingsViewModelOutputs = this

    override val themeChanged: MutableLiveData<Event> = MutableLiveData()

    override val openThemePicker: MutableLiveData<Event> = MutableLiveData()
    override val openAbout: MutableLiveData<Event> = MutableLiveData()
    override val openRelease: MutableLiveData<Event> = MutableLiveData()
    override val openSuggestions: MutableLiveData<Event> = MutableLiveData()
    override val openNews: MutableLiveData<Event> = MutableLiveData()

    override val themePreferences: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()

    init {
        settings.value = listOf(
            AppPreferencesItem.Category(R.string.settings_customisation),
            SettingsOptions.QUALIFYING_DELTAS.toSwitch(prefDB.showQualifyingDelta),
            SettingsOptions.QUALIFYING_GRID_PENALTY.toSwitch(prefDB.showGridPenaltiesInQualifying),
            SettingsOptions.SHOW_DRIVERS_POINTS_IN_CONSTRUCTORS.toSwitch(prefDB.showDriversBehindConstructor),
            AppPreferencesItem.Category(R.string.settings_theme),
            SettingsOptions.THEME.toPref(),
            AppPreferencesItem.Category(R.string.settings_customisation_news),
            SettingsOptions.NEWS.toPref(),
            AppPreferencesItem.Category(R.string.settings_season_list),
            SettingsOptions.SEASON_BOTTOM_SHEET_EXPANDED.toSwitch(prefDB.showBottomSheetExpanded),
            SettingsOptions.SEASON_BOTTOM_SHEET_FAVOURITED.toSwitch(prefDB.showBottomSheetFavourited),
            SettingsOptions.SEASON_BOTTOM_SHEET_ALL.toSwitch(prefDB.showBottomSheetAll),
            AppPreferencesItem.Category(R.string.settings_help),
            SettingsOptions.ABOUT.toPref(),
            SettingsOptions.RELEASE.toPref(),
            AppPreferencesItem.Category(R.string.settings_feedback),
            SettingsOptions.CRASH.toSwitch(prefDB.crashReporting),
            SettingsOptions.SUGGESTION.toPref(),
            SettingsOptions.SHAKE.toSwitch(prefDB.shakeToReport)
        )

        updateThemeList()
    }

    //region Inputs

    override fun preferenceClicked(pref: SettingsOptions?, value: Boolean?) {
        when (pref) {
            SettingsOptions.THEME -> openThemePicker.value = Event()
            SettingsOptions.QUALIFYING_DELTAS -> prefDB.showQualifyingDelta = value ?: false
            SettingsOptions.QUALIFYING_GRID_PENALTY -> prefDB.showGridPenaltiesInQualifying = value ?: true
            SettingsOptions.SHOW_DRIVERS_POINTS_IN_CONSTRUCTORS -> prefDB.showDriversBehindConstructor = value ?: true
            SettingsOptions.SEASON_BOTTOM_SHEET_EXPANDED -> prefDB.showBottomSheetExpanded = value ?: true
            SettingsOptions.SEASON_BOTTOM_SHEET_FAVOURITED -> prefDB.showBottomSheetFavourited = value ?: true
            SettingsOptions.SEASON_BOTTOM_SHEET_ALL -> prefDB.showBottomSheetAll = value ?: true
            SettingsOptions.ABOUT -> openAbout.value = Event()
            SettingsOptions.RELEASE -> openRelease.value = Event()
            SettingsOptions.CRASH -> prefDB.crashReporting = value ?: true
            SettingsOptions.SUGGESTION -> openSuggestions.value = Event()
            SettingsOptions.SHAKE -> prefDB.shakeToReport = value ?: true
            SettingsOptions.NEWS -> openNews.value = Event()
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