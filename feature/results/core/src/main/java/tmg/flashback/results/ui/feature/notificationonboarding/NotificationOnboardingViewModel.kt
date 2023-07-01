package tmg.flashback.results.ui.feature.notificationonboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.results.contract.repository.models.NotificationUpcoming
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.repository.models.icon
import tmg.utilities.models.Selected
import javax.inject.Inject

interface NotificationOnboardingViewModelInputs {
    fun selectNotificationChannel(notificationChannel: NotificationUpcoming)
}

interface NotificationOnboardingViewModelOutputs {
    val notificationPreferences: StateFlow<List<Selected<NotificationOnboardingModel>>>
}

@HiltViewModel
class NotificationOnboardingViewModel @Inject constructor(
    val notificationRepository: NotificationsRepositoryImpl
): ViewModel(), NotificationOnboardingViewModelInputs, NotificationOnboardingViewModelOutputs {

    val inputs: NotificationOnboardingViewModelInputs = this
    val outputs: NotificationOnboardingViewModelOutputs = this

    override val notificationPreferences: MutableStateFlow<List<Selected<NotificationOnboardingModel>>> = MutableStateFlow(emptyList())

    init {
        notificationRepository.seenNotificationOnboarding = true
        updateList()
    }

    //region Inputs

    override fun selectNotificationChannel(upcoming: NotificationUpcoming) {
        notificationRepository.setUpcomingEnabled(upcoming, !notificationRepository.isUpcomingEnabled(upcoming))
        updateList()
    }

    //endregion

    private fun updateList() {
        notificationPreferences.value = NotificationUpcoming.values()
            .map {
                Selected(
                    value = NotificationOnboardingModel(
                        id = it.name,
                        channel = it,
                        name = it.channelLabel,
                        icon = it.icon
                    ),
                    isSelected = notificationRepository.isUpcomingEnabled(it)
                )
            }
    }
}