package tmg.flashback.statistics.ui.dashboard.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.repository.models.NotificationChannel
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

//region Inputs

interface OnboardingNotificationViewModelInputs {
    fun selectNotificationChannel(notificationChannel: NotificationChannel)
}

//endregion

//region Outputs

interface OnboardingNotificationViewModelOutputs {
    val notificationPreferences: LiveData<List<Selected<BottomSheetItem>>>
}

//endregion


class OnboardingNotificationViewModel(
    private val scheduleController: ScheduleController
): ViewModel(), OnboardingNotificationViewModelInputs, OnboardingNotificationViewModelOutputs {

    var inputs: OnboardingNotificationViewModelInputs = this
    var outputs: OnboardingNotificationViewModelOutputs = this

    override val notificationPreferences: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()

    init {
        scheduleController.seenOnboarding()
        updateList()
    }

    //region Inputs

    override fun selectNotificationChannel(notificationChannel: NotificationChannel) {
        when (notificationChannel) {
            NotificationChannel.RACE -> scheduleController.notificationRace = !scheduleController.notificationRace
            NotificationChannel.QUALIFYING -> scheduleController.notificationQualifying = !scheduleController.notificationQualifying
            NotificationChannel.FREE_PRACTICE -> scheduleController.notificationFreePractice = !scheduleController.notificationFreePractice
            NotificationChannel.SEASON_INFO -> scheduleController.notificationSeasonInfo = !scheduleController.notificationSeasonInfo
        }
        updateList()
    }

    //endregion

    private fun updateList() {
        notificationPreferences.value = NotificationChannel.values()
            .map {
                Selected(BottomSheetItem(
                    id = it.ordinal,
                    image = it.icon,
                    text = StringHolder(it.label)
                ),
                when (it) {
                    NotificationChannel.RACE -> scheduleController.notificationRace
                    NotificationChannel.QUALIFYING -> scheduleController.notificationQualifying
                    NotificationChannel.FREE_PRACTICE -> scheduleController.notificationFreePractice
                    NotificationChannel.SEASON_INFO -> scheduleController.notificationSeasonInfo
                })
            }
    }
}
