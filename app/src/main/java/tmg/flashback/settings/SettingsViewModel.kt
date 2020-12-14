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
import tmg.flashback.repo.toggle.ToggleRepository
import tmg.flashback.repo.pref.PrefCustomisationRepository
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.pref.PrefDeviceRepository
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
        private val prefCustomisationRepository: PrefCustomisationRepository,
        private val prefDeviceRepository: PrefDeviceRepository,
        private val toggleDB: ToggleRepository
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
            add(SettingsOptions.QUALIFYING_DELTAS.toSwitch(prefCustomisationRepository.showQualifyingDelta))
            add(SettingsOptions.QUALIFYING_GRID_PENALTY.toSwitch(prefCustomisationRepository.showGridPenaltiesInQualifying))
            add(AppPreferencesItem.Category(R.string.settings_season_list))
            add(SettingsOptions.SEASON_BOTTOM_SHEET_EXPANDED.toSwitch(prefCustomisationRepository.showBottomSheetExpanded))
            add(SettingsOptions.SEASON_BOTTOM_SHEET_FAVOURITED.toSwitch(prefCustomisationRepository.showBottomSheetFavourited))
            add(SettingsOptions.SEASON_BOTTOM_SHEET_ALL.toSwitch(prefCustomisationRepository.showBottomSheetAll))
            add(AppPreferencesItem.Category(R.string.settings_help))
            add(SettingsOptions.ABOUT.toPref())
            add(SettingsOptions.REVIEW.toPref())
            add(SettingsOptions.PRIVACY_POLICY.toPref())
            add(SettingsOptions.RELEASE.toPref())
            add(AppPreferencesItem.Category(R.string.settings_feedback))
            add(SettingsOptions.CRASH.toSwitch(prefDeviceRepository.crashReporting))
            add(SettingsOptions.SUGGESTION.toPref())
            add(SettingsOptions.SHAKE.toSwitch(prefDeviceRepository.shakeToReport))
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
            SettingsOptions.QUALIFYING_DELTAS -> prefCustomisationRepository.showQualifyingDelta = value ?: false
            SettingsOptions.QUALIFYING_GRID_PENALTY -> prefCustomisationRepository.showGridPenaltiesInQualifying = value ?: true
            SettingsOptions.SEASON_BOTTOM_SHEET_EXPANDED -> prefCustomisationRepository.showBottomSheetExpanded = value ?: true
            SettingsOptions.SEASON_BOTTOM_SHEET_FAVOURITED -> prefCustomisationRepository.showBottomSheetFavourited = value ?: true
            SettingsOptions.SEASON_BOTTOM_SHEET_ALL -> prefCustomisationRepository.showBottomSheetAll = value ?: true
            SettingsOptions.BAR_ANIMATION_SPEED -> openAnimationPicker.value = Event()
            SettingsOptions.ABOUT -> openAbout.value = Event()
            SettingsOptions.REVIEW -> openReview.value = DataEvent(playStoreUrl)
            SettingsOptions.PRIVACY_POLICY -> openPrivacyPolicy.value = Event()
            SettingsOptions.RELEASE -> openRelease.value = Event()
            SettingsOptions.CRASH -> prefDeviceRepository.crashReporting = value ?: true
            SettingsOptions.SUGGESTION -> openSuggestions.value = Event()
            SettingsOptions.SHAKE -> prefDeviceRepository.shakeToReport = value ?: true
            SettingsOptions.NEWS -> openNews.value = Event()
        }
    }

    override fun pickTheme(theme: ThemePref) {
        prefCustomisationRepository.theme = theme
        updateThemeList()
        themeChanged.value = Event()
    }

    override fun pickAnimationSpeed(animation: BarAnimation) {
        prefCustomisationRepository.barAnimation = animation
        updateAnimationList()
        animationChanged.value = Event()
    }

    //endregion

    private fun updateThemeList() {
        themePreferences.value = ThemePref.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, it.label), it == prefCustomisationRepository.theme)
                }
    }

    private fun updateAnimationList() {
        animationPreference.value = BarAnimation.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, it.label), it == prefCustomisationRepository.barAnimation)
                }
    }

}