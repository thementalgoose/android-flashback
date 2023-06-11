package tmg.flashback.ui.settings.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.usecases.ResubscribeNotificationsUseCase
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
    val permissionEnabled: StateFlow<Boolean>
    val qualifyingEnabled: StateFlow<Boolean>
    val sprintEnabled: StateFlow<Boolean>
    val raceEnabled: StateFlow<Boolean>
}

@HiltViewModel
class SettingsNotificationsResultsViewModel @Inject constructor(
    private val notificationRepository: NotificationsRepositoryImpl,
    private val resubscribeNotificationsUseCase: ResubscribeNotificationsUseCase,
    private val permissionRepository: PermissionRepository,
    private val permissionManager: PermissionManager,
): ViewModel(), SettingsNotificationsResultsViewModelInputs, SettingsNotificationsResultsViewModelOutputs {

    val inputs: SettingsNotificationsResultsViewModelInputs = this
    val outputs: SettingsNotificationsResultsViewModelOutputs = this

    override val permissionEnabled: MutableStateFlow<Boolean> = MutableStateFlow(permissionRepository.isRuntimeNotificationsEnabled)
    override val qualifyingEnabled: MutableStateFlow<Boolean> = MutableStateFlow(notificationRepository.notificationNotifyQualifying)
    override val sprintEnabled: MutableStateFlow<Boolean> = MutableStateFlow(notificationRepository.notificationNotifySprint)
    override val raceEnabled: MutableStateFlow<Boolean> = MutableStateFlow(notificationRepository.notificationNotifyRace)

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