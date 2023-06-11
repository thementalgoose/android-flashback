package tmg.flashback.ui.settings.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.results.contract.ResultsNavigationComponent
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.usecases.ScheduleNotificationsUseCase
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.permissions.RationaleType
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings
import javax.inject.Inject

interface SettingsNotificationsUpcomingViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsNotificationsUpcomingViewModelOutputs {
    val permissionEnabled: StateFlow<Boolean>
    val freePracticeEnabled: StateFlow<Boolean>
    val qualifyingEnabled: StateFlow<Boolean>
    val sprintEnabled: StateFlow<Boolean>
    val raceEnabled: StateFlow<Boolean>
    val otherEnabled: StateFlow<Boolean>
}

@HiltViewModel
class SettingsNotificationsUpcomingViewModel @Inject constructor(
    private val notificationRepository: NotificationsRepositoryImpl,
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase,
    private val permissionRepository: PermissionRepository,
    private val permissionManager: PermissionManager,
    private val resultsNavigationComponent: ResultsNavigationComponent
): ViewModel(), SettingsNotificationsUpcomingViewModelInputs, SettingsNotificationsUpcomingViewModelOutputs {

    val inputs: SettingsNotificationsUpcomingViewModelInputs = this
    val outputs: SettingsNotificationsUpcomingViewModelOutputs = this

    override val permissionEnabled: MutableStateFlow<Boolean> = MutableStateFlow(permissionRepository.isRuntimeNotificationsEnabled)
    override val freePracticeEnabled: MutableStateFlow<Boolean> = MutableStateFlow(notificationRepository.notificationUpcomingFreePractice)
    override val qualifyingEnabled: MutableStateFlow<Boolean> = MutableStateFlow(notificationRepository.notificationUpcomingQualifying)
    override val sprintEnabled: MutableStateFlow<Boolean> = MutableStateFlow(notificationRepository.notificationUpcomingSprint)
    override val raceEnabled: MutableStateFlow<Boolean> = MutableStateFlow(notificationRepository.notificationUpcomingRace)
    override val otherEnabled: MutableStateFlow<Boolean> = MutableStateFlow(notificationRepository.notificationUpcomingOther)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Notifications.notificationPermissionEnable.key -> {
                if (!permissionRepository.isRuntimeNotificationsEnabled) {
                    permissionManager
                        .requestPermission(RationaleType.RuntimeNotifications)
                        .invokeOnCompletion {
                            if (it != null) {
                                // Open app settings!
                            } else {
                                refresh()
                            }
                        }
                } else {
                    refresh()
                }
            }

            Settings.Notifications.notificationUpcomingOtherKey -> {
                notificationRepository.notificationUpcomingOther = !notificationRepository.notificationUpcomingOther
                otherEnabled.value = notificationRepository.notificationUpcomingOther
                resubscribe()
            }
            Settings.Notifications.notificationUpcomingFreePracticeKey -> {
                notificationRepository.notificationUpcomingFreePractice = !notificationRepository.notificationUpcomingFreePractice
                freePracticeEnabled.value = notificationRepository.notificationUpcomingFreePractice
                resubscribe()
            }
            Settings.Notifications.notificationUpcomingQualifyingKey -> {
                notificationRepository.notificationUpcomingQualifying = !notificationRepository.notificationUpcomingQualifying
                qualifyingEnabled.value = notificationRepository.notificationUpcomingQualifying
                resubscribe()
            }
            Settings.Notifications.notificationUpcomingSprintKey -> {
                notificationRepository.notificationUpcomingSprint = !notificationRepository.notificationUpcomingSprint
                sprintEnabled.value = notificationRepository.notificationUpcomingSprint
                resubscribe()
            }
            Settings.Notifications.notificationUpcomingRaceKey -> {
                notificationRepository.notificationUpcomingRace = !notificationRepository.notificationUpcomingRace
                raceEnabled.value = notificationRepository.notificationUpcomingRace
                resubscribe()
            }

            Settings.Notifications.notificationNoticePeriodKey -> {
                resultsNavigationComponent.upNext()
            }
        }
    }

    private fun resubscribe() {
        scheduleNotificationsUseCase.schedule()
    }

    fun refresh() {
        permissionEnabled.value = permissionRepository.isRuntimeNotificationsEnabled
        resubscribe()
    }
}