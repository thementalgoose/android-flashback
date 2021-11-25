package tmg.flashback.statistics.ui.settings.notifications.reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.flashback.statistics.controllers.UpNextController
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.utilities.lifecycle.Event
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

//region Inputs

interface UpNextReminderViewModelInputs {
    fun selectNotificationReminder(reminder: tmg.flashback.statistics.repository.models.NotificationReminder)
}

//endregion

//region Outputs

interface UpNextReminderViewModelOutputs {
    val notificationPrefs: LiveData<List<Selected<BottomSheetItem>>>
    val updated: LiveData<Event>
}

//endregion


class UpNextReminderViewModel(
    private val upNextController: UpNextController
): ViewModel(), UpNextReminderViewModelInputs, UpNextReminderViewModelOutputs {

    var inputs: UpNextReminderViewModelInputs = this
    var outputs: UpNextReminderViewModelOutputs = this

    override val notificationPrefs: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()
    override val updated: MutableLiveData<Event> = MutableLiveData()

    init {
        updateList()
    }

    //region Inputs

    override fun selectNotificationReminder(reminder: tmg.flashback.statistics.repository.models.NotificationReminder) {
        upNextController.notificationReminder = reminder
        updateList()
        updated.value = Event()
    }

    //endregion

    private fun updateList() {
        notificationPrefs.value = tmg.flashback.statistics.repository.models.NotificationReminder.values()
            .map {
                Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), upNextController.notificationReminder == it)
            }
    }
}
