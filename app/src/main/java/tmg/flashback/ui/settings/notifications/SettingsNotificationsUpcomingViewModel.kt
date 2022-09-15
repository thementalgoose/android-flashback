package tmg.flashback.ui.settings.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.ui.settings.Settings
import tmg.flashback.ui.settings.Setting
import javax.inject.Inject

interface SettingsNotificationsUpcomingViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsNotificationsUpcomingViewModelOutputs {
    val permissionEnabled: LiveData<Boolean>
    val freePracticeEnabled: LiveData<Boolean>
    val qualifyingEnabled: LiveData<Boolean>
    val raceEnabled: LiveData<Boolean>
    val otherEnabled: LiveData<Boolean>
}

@HiltViewModel
class SettingsNotificationsUpcomingViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val statsNavigationComponent: StatsNavigationComponent
): ViewModel(), SettingsNotificationsUpcomingViewModelInputs, SettingsNotificationsUpcomingViewModelOutputs {

    val inputs: SettingsNotificationsUpcomingViewModelInputs = this
    val outputs: SettingsNotificationsUpcomingViewModelOutputs = this

    override val permissionEnabled: MutableLiveData<Boolean> = MutableLiveData()
    override val freePracticeEnabled: MutableLiveData<Boolean> = MutableLiveData(notificationRepository.notificationFreePractice)
    override val qualifyingEnabled: MutableLiveData<Boolean> = MutableLiveData(notificationRepository.notificationQualifying)
    override val raceEnabled: MutableLiveData<Boolean> = MutableLiveData(notificationRepository.notificationRace)
    override val otherEnabled: MutableLiveData<Boolean> = MutableLiveData(notificationRepository.notificationOther)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Notifications.notificationUpcomingOtherKey -> {
                notificationRepository.notificationOther = !notificationRepository.notificationOther
                otherEnabled.value = notificationRepository.notificationOther
            }
            Settings.Notifications.notificationUpcomingFreePracticeKey -> {
                notificationRepository.notificationFreePractice = !notificationRepository.notificationFreePractice
                freePracticeEnabled.value = notificationRepository.notificationFreePractice
            }
            Settings.Notifications.notificationUpcomingQualifyingKey -> {
                notificationRepository.notificationQualifying = !notificationRepository.notificationQualifying
                qualifyingEnabled.value = notificationRepository.notificationQualifying
            }
            Settings.Notifications.notificationUpcomingRaceKey -> {
                notificationRepository.notificationRace = !notificationRepository.notificationRace
                raceEnabled.value = notificationRepository.notificationRace
            }

            Settings.Notifications.notificationNoticePeriodKey -> {
                statsNavigationComponent.upNext()
            }
        }
    }
}