package tmg.flashback.ui.settings.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tmg.flashback.results.contract.repository.models.NotificationResultsAvailable
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.repository.models.prefKey
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
    val notifications: StateFlow<List<Pair<NotificationResultsAvailable, Boolean>>>
    val permissionEnabled: StateFlow<Boolean>
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

    override val notifications: MutableStateFlow<List<Pair<NotificationResultsAvailable, Boolean>>> = MutableStateFlow(
        NotificationResultsAvailable.values().map { it to notificationRepository.isEnabled(it) }
    )
    override val permissionEnabled: MutableStateFlow<Boolean> = MutableStateFlow(permissionRepository.isRuntimeNotificationsEnabled)

    override fun prefClicked(pref: Setting) {

        val notificationResults: NotificationResultsAvailable? = NotificationResultsAvailable.values().firstOrNull { it.prefKey == pref.key }
        if (notificationResults != null) {
            notificationRepository.setEnabled(notificationResults, !notificationRepository.isEnabled(notificationResults))
            refresh()
            return
        }

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
        }
    }

    private fun resubscribe() {
        viewModelScope.launch {
            resubscribeNotificationsUseCase.resubscribe()
        }
    }

    fun refresh() {
        notifications.value = NotificationResultsAvailable.values().map { it to notificationRepository.isEnabled(it) }
        permissionEnabled.value = permissionRepository.isRuntimeNotificationsEnabled
        resubscribe()
    }
}