package tmg.flashback.upnext.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.core.ui.bottomsheet.BottomSheetItem
import tmg.flashback.upnext.controllers.UpNextController
import tmg.flashback.upnext.model.NotificationChannel
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
    private val upNextController: UpNextController
): ViewModel(), OnboardingNotificationViewModelInputs, OnboardingNotificationViewModelOutputs {

    var inputs: OnboardingNotificationViewModelInputs = this
    var outputs: OnboardingNotificationViewModelOutputs = this

    override val notificationPreferences: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()

    init {
        upNextController.seenOnboarding()
        updateList()
    }

    //region Inputs

    override fun selectNotificationChannel(notificationChannel: NotificationChannel) {
        when (notificationChannel) {
            NotificationChannel.RACE -> upNextController.notificationRace = !upNextController.notificationRace
            NotificationChannel.QUALIFYING -> upNextController.notificationQualifying = !upNextController.notificationQualifying
            NotificationChannel.FREE_PRACTICE -> upNextController.notificationFreePractice = !upNextController.notificationFreePractice
            NotificationChannel.SEASON_INFO -> upNextController.notificationSeasonInfo = !upNextController.notificationSeasonInfo
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
                    NotificationChannel.RACE -> upNextController.notificationRace
                    NotificationChannel.QUALIFYING -> upNextController.notificationQualifying
                    NotificationChannel.FREE_PRACTICE -> upNextController.notificationFreePractice
                    NotificationChannel.SEASON_INFO -> upNextController.notificationSeasonInfo
                })
            }
    }
}
