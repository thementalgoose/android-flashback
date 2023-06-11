package tmg.flashback.results.ui.settings.notifications.reminder

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.repository.models.NotificationReminder
import tmg.flashback.results.usecases.ScheduleNotificationsUseCase
import javax.inject.Inject

//region Inputs

interface UpNextReminderViewModelInputs {
    fun selectNotificationReminder(reminder: NotificationReminder)
}

//endregion

//region Outputs

interface UpNextReminderViewModelOutputs {
    val currentlySelected: StateFlow<NotificationReminder>
}

//endregion

@HiltViewModel
class UpNextReminderViewModel @Inject constructor(
    private val notificationRepository: NotificationsRepositoryImpl,
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase
): ViewModel(), UpNextReminderViewModelInputs, UpNextReminderViewModelOutputs {

    var inputs: UpNextReminderViewModelInputs = this
    var outputs: UpNextReminderViewModelOutputs = this

    override val currentlySelected: MutableStateFlow<NotificationReminder> = MutableStateFlow(notificationRepository.notificationReminderPeriod)

    override fun selectNotificationReminder(reminder: NotificationReminder) {
        notificationRepository.notificationReminderPeriod = reminder
        scheduleNotificationsUseCase.schedule()
    }
}
