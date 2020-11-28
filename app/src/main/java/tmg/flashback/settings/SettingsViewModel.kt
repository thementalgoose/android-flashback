package tmg.flashback.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.base.BaseViewModel
import tmg.flashback.di.device.BuildConfigProvider
import tmg.flashback.extensions.icon
import tmg.flashback.extensions.label
import tmg.flashback.notifications.FirebasePushNotificationManager.Companion.topicQualifying
import tmg.flashback.notifications.FirebasePushNotificationManager.Companion.topicRace
import tmg.flashback.repo.ScopeProvider
import tmg.flashback.repo.toggle.ToggleDB
import tmg.flashback.repo.pref.PrefsDB
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.utils.Selected
import tmg.flashback.utils.bottomsheet.BottomSheetItem
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsViewModelInputs {
    fun preferenceClicked(pref: SettingsOptions?, value: Boolean?)
    fun pickTheme(theme: ThemePref)
    fun pickAnimationSpeed(animation: BarAnimation)
}

//endregion

//region Outputs

interface SettingsViewModelOutputs {
    val settings: MutableLiveData<List<AppPreferencesItem>>

    val themePreferences: MutableLiveData<List<Selected<BottomSheetItem>>>
    val animationPreference: MutableLiveData<List<Selected<BottomSheetItem>>>

    val themeChanged: MutableLiveData<Event>
    val animationChanged: MutableLiveData<Event>

    val openThemePicker: MutableLiveData<Event>
    val openAnimationPicker: MutableLiveData<Event>
    val openAbout: MutableLiveData<Event>
    val openRelease: MutableLiveData<Event>
    val openSuggestions: MutableLiveData<Event>
    val openNews: MutableLiveData<Event>

    val openNotificationsChannel: LiveData<DataEvent<String>>
    val openNotifications: LiveData<Event>
}

//endregion

class SettingsViewModel(
    private val prefDB: PrefsDB,
    private val toggleDB: ToggleDB,
    scopeProvider: ScopeProvider
): BaseViewModel(scopeProvider), SettingsViewModelInputs, SettingsViewModelOutputs {

    var inputs: SettingsViewModelInputs = this
    var outputs: SettingsViewModelOutputs = this

    override val themeChanged: MutableLiveData<Event> = MutableLiveData()
    override val animationChanged: MutableLiveData<Event> = MutableLiveData()

    override val themePreferences: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()
    override val animationPreference: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()

    override val openThemePicker: MutableLiveData<Event> = MutableLiveData()
    override val openAnimationPicker: MutableLiveData<Event> = MutableLiveData()
    override val openAbout: MutableLiveData<Event> = MutableLiveData()
    override val openRelease: MutableLiveData<Event> = MutableLiveData()
    override val openSuggestions: MutableLiveData<Event> = MutableLiveData()
    override val openNews: MutableLiveData<Event> = MutableLiveData()

    override val openNotifications: MutableLiveData<Event> = MutableLiveData()
    override val openNotificationsChannel: MutableLiveData<DataEvent<String>> = MutableLiveData()

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()

    init {
        settings.value = mutableListOf<AppPreferencesItem>().apply {
            if (toggleDB.isRSSEnabled) {
                add(AppPreferencesItem.Category(R.string.settings_customisation_rss))
                add(SettingsOptions.NEWS.toPref())
            }
            add(AppPreferencesItem.Category(R.string.settings_notifications_title))
            if (toggleDB.isNotificationChannelsSupported) {
                add(SettingsOptions.NOTIFICATIONS_CHANNEL_QUALIFYING.toPref())
                add(SettingsOptions.NOTIFICATIONS_CHANNEL_RACE.toPref())
            }
            else {
                add(SettingsOptions.NOTIFICATIONS_SETTINGS.toPref())
            }
            add(AppPreferencesItem.Category(R.string.settings_theme))
            add(SettingsOptions.THEME.toPref())
            add(AppPreferencesItem.Category(R.string.settings_customisation))
            add(SettingsOptions.BAR_ANIMATION_SPEED.toPref())
            add(SettingsOptions.QUALIFYING_DELTAS.toSwitch(prefDB.showQualifyingDelta))
            add(SettingsOptions.QUALIFYING_GRID_PENALTY.toSwitch(prefDB.showGridPenaltiesInQualifying))
            add(AppPreferencesItem.Category(R.string.settings_season_list))
            add(SettingsOptions.SEASON_BOTTOM_SHEET_EXPANDED.toSwitch(prefDB.showBottomSheetExpanded))
            add(SettingsOptions.SEASON_BOTTOM_SHEET_FAVOURITED.toSwitch(prefDB.showBottomSheetFavourited))
            add(SettingsOptions.SEASON_BOTTOM_SHEET_ALL.toSwitch(prefDB.showBottomSheetAll))
            add(AppPreferencesItem.Category(R.string.settings_help))
            add(SettingsOptions.ABOUT.toPref())
            add(SettingsOptions.RELEASE.toPref())
            add(AppPreferencesItem.Category(R.string.settings_feedback))
            add(SettingsOptions.CRASH.toSwitch(prefDB.crashReporting))
            add(SettingsOptions.SUGGESTION.toPref())
            add(SettingsOptions.SHAKE.toSwitch(prefDB.shakeToReport))
        }

        updateThemeList()
        updateAnimationList()
    }

    //region Inputs

    override fun preferenceClicked(pref: SettingsOptions?, value: Boolean?) {
        when (pref) {
            SettingsOptions.THEME -> openThemePicker.value = Event()
            SettingsOptions.NOTIFICATIONS_CHANNEL_RACE -> openNotificationsChannel.value = DataEvent(topicRace)
            SettingsOptions.NOTIFICATIONS_CHANNEL_QUALIFYING -> openNotificationsChannel.value = DataEvent(topicQualifying)
            SettingsOptions.NOTIFICATIONS_SETTINGS -> openNotifications.value = Event()
            SettingsOptions.QUALIFYING_DELTAS -> prefDB.showQualifyingDelta = value ?: false
            SettingsOptions.QUALIFYING_GRID_PENALTY -> prefDB.showGridPenaltiesInQualifying = value ?: true
            SettingsOptions.SEASON_BOTTOM_SHEET_EXPANDED -> prefDB.showBottomSheetExpanded = value ?: true
            SettingsOptions.SEASON_BOTTOM_SHEET_FAVOURITED -> prefDB.showBottomSheetFavourited = value ?: true
            SettingsOptions.SEASON_BOTTOM_SHEET_ALL -> prefDB.showBottomSheetAll = value ?: true
            SettingsOptions.BAR_ANIMATION_SPEED -> openAnimationPicker.value = Event()
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

    override fun pickAnimationSpeed(animation: BarAnimation) {
        prefDB.barAnimation = animation
        updateAnimationList()
        animationChanged.value = Event()
    }

    //endregion

    private fun updateThemeList() {
        themePreferences.value = ThemePref.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, it.label), it == prefDB.theme)
                }
    }

    private fun updateAnimationList() {
        animationPreference.value = BarAnimation.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, it.label), it == prefDB.barAnimation)
                }
    }

}