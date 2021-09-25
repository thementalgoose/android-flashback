package tmg.flashback.upnext.ui.settings.reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.core.ui.bottomsheet.BottomSheetItem
import tmg.flashback.upnext.controllers.UpNextController
import tmg.flashback.upnext.model.NotificationReminder
import tmg.utilities.lifecycle.Event
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

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

    override fun selectNotificationReminder(reminder: NotificationReminder) {
        upNextController.notificationReminder = reminder
        updateList()
        updated.value = Event()
    }

    //endregion

    private fun updateList() {
        notificationPrefs.value = NotificationReminder.values()
            .map {
                Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), upNextController.notificationReminder == it)
            }
    }
}
