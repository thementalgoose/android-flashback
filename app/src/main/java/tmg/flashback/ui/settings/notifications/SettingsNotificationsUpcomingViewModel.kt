package tmg.flashback.ui.settings.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.usecases.ResubscribeNotificationsUseCase
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.permissions.RationaleType
import tmg.flashback.ui.repository.PermissionRepository
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
    private val resubscribeNotificationsUseCase: ResubscribeNotificationsUseCase,
    private val permissionRepository: PermissionRepository,
    private val permissionManager: PermissionManager,
    private val statsNavigationComponent: StatsNavigationComponent
): ViewModel(), SettingsNotificationsUpcomingViewModelInputs, SettingsNotificationsUpcomingViewModelOutputs {

    val inputs: SettingsNotificationsUpcomingViewModelInputs = this
    val outputs: SettingsNotificationsUpcomingViewModelOutputs = this

    override val permissionEnabled: MutableLiveData<Boolean> = MutableLiveData(permissionRepository.isRuntimeNotificationsEnabled)
    override val freePracticeEnabled: MutableLiveData<Boolean> = MutableLiveData(notificationRepository.notificationUpcomingFreePractice)
    override val qualifyingEnabled: MutableLiveData<Boolean> = MutableLiveData(notificationRepository.notificationUpcomingQualifying)
    override val raceEnabled: MutableLiveData<Boolean> = MutableLiveData(notificationRepository.notificationUpcomingRace)
    override val otherEnabled: MutableLiveData<Boolean> = MutableLiveData(notificationRepository.notificationUpcomingOther)

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
            Settings.Notifications.notificationUpcomingRaceKey -> {
                notificationRepository.notificationUpcomingRace = !notificationRepository.notificationUpcomingRace
                raceEnabled.value = notificationRepository.notificationUpcomingRace
                resubscribe()
            }

            Settings.Notifications.notificationNoticePeriodKey -> {
                statsNavigationComponent.upNext()
            }
        }
    }

    private fun resubscribe() {
        viewModelScope.launch {
            resubscribeNotificationsUseCase.resubscribe()
        }
    }

    fun refresh() {
        permissionEnabled.value = permissionRepository.isRuntimeNotificationsEnabled
        resubscribe()
    }
}