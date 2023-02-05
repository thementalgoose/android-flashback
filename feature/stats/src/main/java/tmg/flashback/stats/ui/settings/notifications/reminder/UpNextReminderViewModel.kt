package tmg.flashback.stats.ui.settings.notifications.reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.repository.models.NotificationReminder
import tmg.flashback.stats.usecases.ScheduleNotificationsUseCase
import javax.inject.Inject

//region Inputs

interface UpNextReminderViewModelInputs {
    fun selectNotificationReminder(reminder: NotificationReminder)
}

//endregion

//region Outputs

interface UpNextReminderViewModelOutputs {
    val currentlySelected: LiveData<NotificationReminder>
}

//endregion

@HiltViewModel
class UpNextReminderViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase
): ViewModel(), UpNextReminderViewModelInputs, UpNextReminderViewModelOutputs {

    var inputs: UpNextReminderViewModelInputs = this
    var outputs: UpNextReminderViewModelOutputs = this

    override val currentlySelected: MutableLiveData<NotificationReminder> = MutableLiveData()

    init {
        currentlySelected.value = notificationRepository.notificationReminderPeriod
    }

    override fun selectNotificationReminder(reminder: NotificationReminder) {
        notificationRepository.notificationReminderPeriod = reminder
        scheduleNotificationsUseCase.schedule()
    }
}
