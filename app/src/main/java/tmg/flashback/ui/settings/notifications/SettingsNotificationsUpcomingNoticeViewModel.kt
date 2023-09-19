package tmg.flashback.ui.settings.notifications

import android.os.Build
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.results.contract.repository.NotificationsRepository
import tmg.flashback.results.contract.repository.models.NotificationReminder
import tmg.flashback.results.usecases.ScheduleNotificationsUseCase
import tmg.flashback.device.AppPermissions
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.device.repository.PermissionRepository
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings
import javax.inject.Inject

//region Inputs

interface SettingsNotificationsUpcomingNoticeViewModelInputs {
    fun refresh()
    fun prefClicked(reminder: Setting)
}

//endregion

//region Outputs

interface SettingsNotificationsUpcomingNoticeViewModelOutputs {
    val currentlySelected: StateFlow<NotificationReminder>
    val permissions: StateFlow<UpcomingNoticePermissionState>
}

//endregion

@HiltViewModel
class SettingsNotificationsUpcomingNoticeViewModel @Inject constructor(
    private val notificationRepository: NotificationsRepository,
    private val permissionManager: PermissionManager,
    private val permissionRepository: PermissionRepository,
    private val applicationNavigationComponent: ApplicationNavigationComponent,
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase,
): ViewModel(), SettingsNotificationsUpcomingNoticeViewModelInputs, SettingsNotificationsUpcomingNoticeViewModelOutputs {

    var inputs: SettingsNotificationsUpcomingNoticeViewModelInputs = this
    var outputs: SettingsNotificationsUpcomingNoticeViewModelOutputs = this

    override val currentlySelected: MutableStateFlow<NotificationReminder> = MutableStateFlow(notificationRepository.notificationReminderPeriod)
    override val permissions: MutableStateFlow<UpcomingNoticePermissionState> = MutableStateFlow(getPermissionState())

    override fun prefClicked(reminder: Setting) {
        when (reminder.key) {
            Settings.Notifications.notificationPermissionEnable.key -> {
                permissionManager
                    .requestPermission(AppPermissions.RuntimeNotifications)
                    .invokeOnCompletion {
                        refresh()
                    }
            }
            Settings.Notifications.notificationExactAlarmEnable.key -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    applicationNavigationComponent.appSettingsSpecialPermissions()
                }
            }
            NotificationReminder.MINUTES_15.name -> {
                notificationRepository.notificationReminderPeriod = NotificationReminder.MINUTES_15
            }
            NotificationReminder.MINUTES_30.name -> {
                notificationRepository.notificationReminderPeriod = NotificationReminder.MINUTES_30
            }
            NotificationReminder.MINUTES_60.name -> {
                notificationRepository.notificationReminderPeriod = NotificationReminder.MINUTES_60
            }
        }
        refresh()
    }

    override fun refresh() {
        currentlySelected.value = notificationRepository.notificationReminderPeriod
        permissions.value = getPermissionState()
        scheduleNotificationsUseCase.schedule()
    }

    private fun getPermissionState() = UpcomingNoticePermissionState(
        runtimePermission = permissionRepository.isRuntimeNotificationsEnabled,
        exactAlarmPermission = permissionRepository.isExactAlarmEnabled
    )
}

data class UpcomingNoticePermissionState(
    val runtimePermission: Boolean,
    val exactAlarmPermission: Boolean
)
