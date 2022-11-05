package tmg.flashback.stats.ui.settings.notifications.reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.repository.models.NotificationReminder
import tmg.flashback.stats.usecases.ScheduleNotificationsUseCase
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.utilities.lifecycle.Event
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder
import javax.inject.Inject

//region Inputs

interface UpNextReminderViewModelInputs {
    fun selectNotificationReminder(reminder: NotificationReminder)
}

//endregion

//region Outputs

interface UpNextReminderViewModelOutputs {
    val notificationPrefs: LiveData<List<Selected<BottomSheetItem>>>
    val updated: LiveData<Event>
}

//endregion

@HiltViewModel
class UpNextReminderViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase
): ViewModel(), UpNextReminderViewModelInputs, UpNextReminderViewModelOutputs {

    var inputs: UpNextReminderViewModelInputs = this
    var outputs: UpNextReminderViewModelOutputs = this

    override val notificationPrefs: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()
    override val updated: MutableLiveData<Event> = MutableLiveData()

    init {
        updateList()
    }

    //region Inputs

    override fun selectNotificationReminder(reminder: NotificationReminder) {
        notificationRepository.notificationReminderPeriod = reminder
        updateList()
        scheduleNotificationsUseCase.schedule()
        updated.value = Event()
    }

    //endregion

    private fun updateList() {
        notificationPrefs.value = NotificationReminder.values()
            .map {
                Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), notificationRepository.notificationReminderPeriod == it)
            }
    }
}
