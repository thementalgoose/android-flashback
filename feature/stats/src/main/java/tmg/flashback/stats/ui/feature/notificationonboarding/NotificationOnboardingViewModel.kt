package tmg.flashback.stats.ui.feature.notificationonboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.repository.models.NotificationChannel
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

interface NotificationOnboardingViewModelInputs {
    fun selectNotificationChannel(notificationChannel: NotificationChannel)
}

interface NotificationOnboardingViewModelOutputs {
    val notificationPreferences: LiveData<List<Selected<BottomSheetItem>>>
}

class NotificationOnboardingViewModel(
    val notificationRepository: NotificationRepository
): ViewModel(), NotificationOnboardingViewModelInputs, NotificationOnboardingViewModelOutputs {

    val inputs: NotificationOnboardingViewModelInputs = this
    val outputs: NotificationOnboardingViewModelOutputs = this

    override val notificationPreferences: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()

    init {
        notificationRepository.seenNotificationOnboarding = true
        updateList()
    }

    //region Inputs

    override fun selectNotificationChannel(notificationChannel: NotificationChannel) {
        when (notificationChannel) {
            NotificationChannel.RACE -> notificationRepository.notificationRace = !notificationRepository.notificationRace
            NotificationChannel.QUALIFYING -> notificationRepository.notificationQualifying = !notificationRepository.notificationQualifying
            NotificationChannel.FREE_PRACTICE -> notificationRepository.notificationFreePractice = !notificationRepository.notificationFreePractice
            NotificationChannel.SEASON_INFO -> notificationRepository.notificationOther = !notificationRepository.notificationOther
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
                        NotificationChannel.RACE -> notificationRepository.notificationRace
                        NotificationChannel.QUALIFYING -> notificationRepository.notificationQualifying
                        NotificationChannel.FREE_PRACTICE -> notificationRepository.notificationFreePractice
                        NotificationChannel.SEASON_INFO -> notificationRepository.notificationOther
                    })
            }
    }

}