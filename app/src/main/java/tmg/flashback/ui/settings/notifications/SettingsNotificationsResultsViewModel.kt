package tmg.flashback.ui.settings.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.usecases.ResubscribeNotificationsUseCase
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.permissions.RationaleType
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings
import javax.inject.Inject

interface SettingsNotificationsResultsViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsNotificationsResultsViewModelOutputs {
    val permissionEnabled: LiveData<Boolean>
    val qualifyingEnabled: LiveData<Boolean>
    val sprintEnabled: LiveData<Boolean>
    val raceEnabled: LiveData<Boolean>
}

@HiltViewModel
class SettingsNotificationsResultsViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val resubscribeNotificationsUseCase: ResubscribeNotificationsUseCase,
    private val permissionRepository: PermissionRepository,
    private val permissionManager: PermissionManager,
): ViewModel(), SettingsNotificationsResultsViewModelInputs, SettingsNotificationsResultsViewModelOutputs {

    val inputs: SettingsNotificationsResultsViewModelInputs = this
    val outputs: SettingsNotificationsResultsViewModelOutputs = this

    override val permissionEnabled: MutableLiveData<Boolean> = MutableLiveData(permissionRepository.isRuntimeNotificationsEnabled)
    override val qualifyingEnabled: MutableLiveData<Boolean> = MutableLiveData(notificationRepository.notificationNotifyQualifying)
    override val sprintEnabled: MutableLiveData<Boolean> = MutableLiveData(notificationRepository.notificationNotifySprint)
    override val raceEnabled: MutableLiveData<Boolean> = MutableLiveData(notificationRepository.notificationNotifyRace)

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
            Settings.Notifications.notificationResultsQualifyingKey -> {
                notificationRepository.notificationNotifyQualifying = !notificationRepository.notificationNotifyQualifying
                qualifyingEnabled.value = notificationRepository.notificationNotifyQualifying
                resubscribe()
            }
            Settings.Notifications.notificationResultsSprintKey -> {
                notificationRepository.notificationNotifySprint = !notificationRepository.notificationNotifySprint
                sprintEnabled.value = notificationRepository.notificationNotifySprint
                resubscribe()
            }
            Settings.Notifications.notificationResultsRaceKey -> {
                notificationRepository.notificationNotifyRace = !notificationRepository.notificationNotifyRace
                raceEnabled.value = notificationRepository.notificationNotifyRace
                resubscribe()
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