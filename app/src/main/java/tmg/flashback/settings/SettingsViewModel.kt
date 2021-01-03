package tmg.flashback.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.base.BaseViewModel
import tmg.flashback.extensions.icon
import tmg.flashback.extensions.label
import tmg.flashback.notifications.FirebasePushNotificationManager.Companion.topicQualifying
import tmg.flashback.notifications.FirebasePushNotificationManager.Companion.topicRace
import tmg.flashback.playStoreUrl
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.UserRepository
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.pref.DeviceRepository
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
        private val userRepository: UserRepository,
        private val deviceRepository: DeviceRepository,
        private val remoteConfigRepository: RemoteConfigRepository
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
            if (remoteConfigRepository.rss) {
                add(AppPreferencesItem.Category(R.string.settings_customisation_rss))
                add(SettingsOptions.NEWS.toPref())
            }
            add(AppPreferencesItem.Category(R.string.settings_notifications_title))
            if (deviceRepository.isNotificationChannelsSupported) {
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
            add(SettingsOptions.QUALIFYING_DELTAS.toSwitch(userRepository.showQualifyingDelta))
            add(SettingsOptions.FADE_OUT_DNF.toSwitch(userRepository.fadeDNF))
            add(SettingsOptions.QUALIFYING_GRID_PENALTY.toSwitch(userRepository.showGridPenaltiesInQualifying))
            add(AppPreferencesItem.Category(R.string.settings_season_list))
            add(SettingsOptions.SEASON_BOTTOM_SHEET_FAVOURITED.toSwitch(userRepository.showListFavourited))
            add(SettingsOptions.SEASON_BOTTOM_SHEET_ALL.toSwitch(userRepository.showListAll))
            add(AppPreferencesItem.Category(R.string.settings_help))
            add(SettingsOptions.ABOUT.toPref())
            add(SettingsOptions.REVIEW.toPref())
            add(SettingsOptions.PRIVACY_POLICY.toPref())
            add(SettingsOptions.RELEASE.toPref())
            add(AppPreferencesItem.Category(R.string.settings_feedback))
            add(SettingsOptions.CRASH.toSwitch(deviceRepository.crashReporting))
            add(SettingsOptions.SUGGESTION.toPref())
            add(SettingsOptions.SHAKE.toSwitch(deviceRepository.shakeToReport))
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
            SettingsOptions.QUALIFYING_DELTAS -> userRepository.showQualifyingDelta = value ?: false
            SettingsOptions.FADE_OUT_DNF -> userRepository.fadeDNF = value ?: false
            SettingsOptions.QUALIFYING_GRID_PENALTY -> userRepository.showGridPenaltiesInQualifying = value ?: true
            SettingsOptions.SEASON_BOTTOM_SHEET_FAVOURITED -> userRepository.showListFavourited = value ?: true
            SettingsOptions.SEASON_BOTTOM_SHEET_ALL -> userRepository.showListAll = value ?: true
            SettingsOptions.BAR_ANIMATION_SPEED -> openAnimationPicker.value = Event()
            SettingsOptions.ABOUT -> openAbout.value = Event()
            SettingsOptions.REVIEW -> openReview.value = DataEvent(playStoreUrl)
            SettingsOptions.PRIVACY_POLICY -> openPrivacyPolicy.value = Event()
            SettingsOptions.RELEASE -> openRelease.value = Event()
            SettingsOptions.CRASH -> deviceRepository.crashReporting = value ?: true
            SettingsOptions.SUGGESTION -> openSuggestions.value = Event()
            SettingsOptions.SHAKE -> deviceRepository.shakeToReport = value ?: true
            SettingsOptions.NEWS -> openNews.value = Event()
        }
    }

    override fun pickTheme(theme: ThemePref) {
        userRepository.theme = theme
        updateThemeList()
        themeChanged.value = Event()
    }

    override fun pickAnimationSpeed(animation: BarAnimation) {
        userRepository.barAnimation = animation
        updateAnimationList()
        animationChanged.value = Event()
    }

    //endregion

    private fun updateThemeList() {
        themePreferences.value = ThemePref.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, it.label), it == userRepository.theme)
                }
    }

    private fun updateAnimationList() {
        animationPreference.value = BarAnimation.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, it.label), it == userRepository.barAnimation)
                }
    }

}