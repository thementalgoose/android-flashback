package tmg.flashback.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.base.BaseViewModel
import tmg.flashback.controllers.*
import tmg.flashback.extensions.icon
import tmg.flashback.extensions.label
import tmg.flashback.notifications.FirebasePushNotificationManager.Companion.topicQualifying
import tmg.flashback.notifications.FirebasePushNotificationManager.Companion.topicRace
import tmg.flashback.playStoreUrl
import tmg.flashback.repo.db.CrashManager
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
    val settings: LiveData<List<AppPreferencesItem>>

    val themePreferences: LiveData<List<Selected<BottomSheetItem>>>
    val animationPreference: LiveData<List<Selected<BottomSheetItem>>>

    val themeChanged: LiveData<Event>
    val animationChanged: LiveData<Event>

    val openThemePicker: LiveData<Event>
    val openAnimationPicker: LiveData<Event>
    val openAbout: LiveData<Event>
    val openReview: LiveData<DataEvent<String>>
    val openPrivacyPolicy: LiveData<Event>
    val openRelease: LiveData<Event>
    val openSuggestions: LiveData<Event>
    val openNews: LiveData<Event>

    val openNotificationsChannel: LiveData<DataEvent<String>>
    val openNotifications: LiveData<Event>
}

//endregion

class SettingsViewModel(
        private val notificationController: NotificationController,
        private val appearanceController: AppearanceController,
        private val deviceController: DeviceController,
        private val raceController: RaceController,
        private val crashManager: CrashManager,
        private val seasonController: SeasonController,
        private val featureController: FeatureController
): BaseViewModel(), SettingsViewModelInputs, SettingsViewModelOutputs {

    var inputs: SettingsViewModelInputs = this
    var outputs: SettingsViewModelOutputs = this

    override val themeChanged: MutableLiveData<Event> = MutableLiveData()
    override val animationChanged: MutableLiveData<Event> = MutableLiveData()

    override val themePreferences: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()
    override val animationPreference: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()

    override val openThemePicker: MutableLiveData<Event> = MutableLiveData()
    override val openAnimationPicker: MutableLiveData<Event> = MutableLiveData()
    override val openAbout: MutableLiveData<Event> = MutableLiveData()
    override val openReview: MutableLiveData<DataEvent<String>> = MutableLiveData()
    override val openPrivacyPolicy: MutableLiveData<Event> = MutableLiveData()
    override val openRelease: MutableLiveData<Event> = MutableLiveData()
    override val openSuggestions: MutableLiveData<Event> = MutableLiveData()
    override val openNews: MutableLiveData<Event> = MutableLiveData()

    override val openNotifications: MutableLiveData<Event> = MutableLiveData()
    override val openNotificationsChannel: MutableLiveData<DataEvent<String>> = MutableLiveData()

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()

    init {
        settings.value = mutableListOf<AppPreferencesItem>().apply {
            if (featureController.rssEnabled) {
                add(AppPreferencesItem.Category(R.string.settings_customisation_rss))
                add(SettingsOptions.NEWS.toPref())
            }
            add(AppPreferencesItem.Category(R.string.settings_notifications_title))
            if (notificationController.isNotificationChannelsSupported) {
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
            add(SettingsOptions.QUALIFYING_DELTAS.toSwitch(raceController.showQualifyingDelta))
            add(SettingsOptions.FADE_OUT_DNF.toSwitch(raceController.fadeDNF))
            add(SettingsOptions.QUALIFYING_GRID_PENALTY.toSwitch(raceController.showGridPenaltiesInQualifying))
            add(AppPreferencesItem.Category(R.string.settings_season_list))
            add(SettingsOptions.SEASON_BOTTOM_SHEET_FAVOURITED.toSwitch(seasonController.defaultFavouritesExpanded))
            add(SettingsOptions.SEASON_BOTTOM_SHEET_ALL.toSwitch(seasonController.defaultAllExpanded))
            add(AppPreferencesItem.Category(R.string.settings_help))
            add(SettingsOptions.ABOUT.toPref())
            add(SettingsOptions.REVIEW.toPref())
            add(SettingsOptions.PRIVACY_POLICY.toPref())
            add(SettingsOptions.RELEASE.toPref())
            add(AppPreferencesItem.Category(R.string.settings_feedback))
            add(SettingsOptions.CRASH.toSwitch(crashManager.enableCrashReporting))
            add(SettingsOptions.SUGGESTION.toPref())
            add(SettingsOptions.SHAKE.toSwitch(deviceController.shakeToReport))
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
            SettingsOptions.QUALIFYING_DELTAS -> raceController.showQualifyingDelta = value ?: false
            SettingsOptions.FADE_OUT_DNF -> raceController.fadeDNF = value ?: false
            SettingsOptions.QUALIFYING_GRID_PENALTY -> raceController.showGridPenaltiesInQualifying = value ?: true
            SettingsOptions.SEASON_BOTTOM_SHEET_FAVOURITED -> seasonController.defaultFavouritesExpanded = value ?: true
            SettingsOptions.SEASON_BOTTOM_SHEET_ALL -> seasonController.defaultAllExpanded = value ?: true
            SettingsOptions.BAR_ANIMATION_SPEED -> openAnimationPicker.value = Event()
            SettingsOptions.ABOUT -> openAbout.value = Event()
            SettingsOptions.REVIEW -> openReview.value = DataEvent(playStoreUrl)
            SettingsOptions.PRIVACY_POLICY -> openPrivacyPolicy.value = Event()
            SettingsOptions.RELEASE -> openRelease.value = Event()
            SettingsOptions.CRASH -> crashManager.enableCrashReporting = value ?: true
            SettingsOptions.SUGGESTION -> openSuggestions.value = Event()
            SettingsOptions.SHAKE -> deviceController.shakeToReport = value ?: true
            SettingsOptions.NEWS -> openNews.value = Event()
        }
    }

    override fun pickTheme(theme: ThemePref) {
        appearanceController.currentTheme = theme
        updateThemeList()
        themeChanged.value = Event()
    }

    override fun pickAnimationSpeed(animation: BarAnimation) {
        appearanceController.barAnimation = animation
        updateAnimationList()
        animationChanged.value = Event()
    }

    //endregion

    private fun updateThemeList() {
        themePreferences.value = ThemePref.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, it.label), it == appearanceController.currentTheme)
                }
    }

    private fun updateAnimationList() {
        animationPreference.value = BarAnimation.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, it.label), it == appearanceController.barAnimation)
                }
    }

}