package tmg.flashback.stats.ui.feature.notificationonboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.repository.models.NotificationChannel
import tmg.utilities.models.Selected
import javax.inject.Inject

interface NotificationOnboardingViewModelInputs {
    fun selectNotificationChannel(notificationChannel: NotificationChannel)
}

interface NotificationOnboardingViewModelOutputs {
    val notificationPreferences: LiveData<List<Selected<NotificationOnboardingModel>>>
}

@HiltViewModel
class NotificationOnboardingViewModel @Inject constructor(
    val notificationRepository: NotificationRepository
): ViewModel(), NotificationOnboardingViewModelInputs, NotificationOnboardingViewModelOutputs {

    val inputs: NotificationOnboardingViewModelInputs = this
    val outputs: NotificationOnboardingViewModelOutputs = this

    override val notificationPreferences: MutableLiveData<List<Selected<NotificationOnboardingModel>>> = MutableLiveData()

    init {
        notificationRepository.seenNotificationOnboarding = true
        updateList()
    }

    //region Inputs

    override fun selectNotificationChannel(notificationChannel: NotificationChannel) {
        when (notificationChannel) {
            NotificationChannel.RACE -> notificationRepository.notificationUpcomingRace = !notificationRepository.notificationUpcomingRace
            NotificationChannel.SPRINT -> notificationRepository.notificationUpcomingSprint = !notificationRepository.notificationUpcomingSprint
            NotificationChannel.QUALIFYING -> notificationRepository.notificationUpcomingQualifying = !notificationRepository.notificationUpcomingQualifying
            NotificationChannel.FREE_PRACTICE -> notificationRepository.notificationUpcomingFreePractice = !notificationRepository.notificationUpcomingFreePractice
            NotificationChannel.SEASON_INFO -> notificationRepository.notificationUpcomingOther = !notificationRepository.notificationUpcomingOther
        }
        updateList()
    }

    //endregion

    private fun updateList() {
        notificationPreferences.value = NotificationChannel.values()
            .map {
                Selected(
                    NotificationOnboardingModel(id = it.name, channel = it, name = it.label, icon = it.icon),
                    when (it) {
                        NotificationChannel.RACE -> notificationRepository.notificationUpcomingRace
                        NotificationChannel.SPRINT -> notificationRepository.notificationUpcomingSprint
                        NotificationChannel.QUALIFYING -> notificationRepository.notificationUpcomingQualifying
                        NotificationChannel.FREE_PRACTICE -> notificationRepository.notificationUpcomingFreePractice
                        NotificationChannel.SEASON_INFO -> notificationRepository.notificationUpcomingOther
                    }
                )
            }
    }
}