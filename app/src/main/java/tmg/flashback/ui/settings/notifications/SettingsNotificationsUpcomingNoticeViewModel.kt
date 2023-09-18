package tmg.flashback.ui.settings.notifications

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.repository.models.NotificationReminder
import tmg.flashback.results.usecases.ScheduleNotificationsUseCase
import tmg.flashback.ui.settings.Setting
import javax.inject.Inject

//region Inputs

interface SettingsNotificationsUpcomingNoticeViewModelInputs {
    fun refresh()
    fun prefClicked(reminder: Setting.Option)
}

//endregion

//region Outputs

interface SettingsNotificationsUpcomingNoticeViewModelOutputs {
    val currentlySelected: StateFlow<NotificationReminder>
}

//endregion

@HiltViewModel
class SettingsNotificationsUpcomingNoticeViewModel @Inject constructor(
    private val notificationRepository: NotificationsRepositoryImpl,
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase
): ViewModel(), SettingsNotificationsUpcomingNoticeViewModelInputs, SettingsNotificationsUpcomingNoticeViewModelOutputs {

    var inputs: SettingsNotificationsUpcomingNoticeViewModelInputs = this
    var outputs: SettingsNotificationsUpcomingNoticeViewModelOutputs = this

    override val currentlySelected: MutableStateFlow<NotificationReminder> = MutableStateFlow(notificationRepository.notificationReminderPeriod)

    override fun prefClicked(reminder: Setting.Option) {
        val option = NotificationReminder.values().firstOrNull { it.name == reminder.key } ?: return
        notificationRepository.notificationReminderPeriod = option
        scheduleNotificationsUseCase.schedule()
        refresh()
    }

    override fun refresh() {
        currentlySelected.value = notificationRepository.notificationReminderPeriod
    }
}
