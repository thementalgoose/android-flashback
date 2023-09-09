package tmg.flashback.ui.settings.notifications

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.navigation.IntentNavigationComponent
import tmg.flashback.results.contract.ResultsNavigationComponent
import tmg.flashback.results.contract.repository.models.NotificationUpcoming
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.repository.models.prefKey
import tmg.flashback.results.usecases.ScheduleNotificationsUseCase
import tmg.flashback.ui.AppPermissions
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings
import javax.inject.Inject

interface SettingsNotificationsUpcomingViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsNotificationsUpcomingViewModelOutputs {
    val notifications: StateFlow<List<Pair<NotificationUpcoming, Boolean>>>
    val permissionEnabled: StateFlow<Boolean>
    val exactAlarmEnabled: StateFlow<Boolean>
}

@HiltViewModel
class SettingsNotificationsUpcomingViewModel @Inject constructor(
    private val notificationRepository: NotificationsRepositoryImpl,
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase,
    private val permissionRepository: PermissionRepository,
    private val permissionManager: PermissionManager,
    private val resultsNavigationComponent: ResultsNavigationComponent,
    private val intentNavigationComponent: IntentNavigationComponent,
): ViewModel(), SettingsNotificationsUpcomingViewModelInputs, SettingsNotificationsUpcomingViewModelOutputs {

    val inputs: SettingsNotificationsUpcomingViewModelInputs = this
    val outputs: SettingsNotificationsUpcomingViewModelOutputs = this

    override val notifications: MutableStateFlow<List<Pair<NotificationUpcoming, Boolean>>> = MutableStateFlow(
        NotificationUpcoming.values().map { it to notificationRepository.isUpcomingEnabled(it) }
    )
    override val permissionEnabled: MutableStateFlow<Boolean> = MutableStateFlow(permissionRepository.isRuntimeNotificationsEnabled)
    override val exactAlarmEnabled: MutableStateFlow<Boolean> = MutableStateFlow(permissionRepository.isExactAlarmEnabled)

    override fun prefClicked(pref: Setting) {
        val upcoming: NotificationUpcoming? = NotificationUpcoming.values().firstOrNull { it.prefKey == pref.key }
        if (upcoming != null) {
            notificationRepository.setUpcomingEnabled(upcoming, !notificationRepository.isUpcomingEnabled(upcoming))
            refresh()
            return
        }

        when (pref.key) {
            Settings.Notifications.notificationPermissionEnable.key -> {
                if (!permissionRepository.isRuntimeNotificationsEnabled) {
                    permissionManager
                        .requestPermission(AppPermissions.RuntimeNotifications)
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
            Settings.Notifications.notificationExactAlarmEnable.key -> {
                if (!permissionRepository.isExactAlarmEnabled) {
                    val intent = AppPermissions.ScheduleExactAlarms.getIntent()
                    intentNavigationComponent.openIntent(intent)
                } else {
                    refresh()
                }
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
        notifications.value = NotificationUpcoming.values().map { it to notificationRepository.isUpcomingEnabled(it) }
        permissionEnabled.value = permissionRepository.isRuntimeNotificationsEnabled
        exactAlarmEnabled.value = permissionRepository.isExactAlarmEnabled
        resubscribe()
    }
}