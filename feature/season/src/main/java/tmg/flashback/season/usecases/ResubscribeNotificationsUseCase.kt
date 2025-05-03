package tmg.flashback.season.usecases

import tmg.flashback.data.repo.NotificationsRepository
import tmg.flashback.notifications.usecases.RemoteNotificationSubscribeUseCase
import tmg.flashback.notifications.usecases.RemoteNotificationUnsubscribeUseCase
import tmg.flashback.formula1.model.notifications.NotificationResultsAvailable
import javax.inject.Inject

class ResubscribeNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationsRepository,
    private val remoteNotificationSubscribeUseCase: RemoteNotificationSubscribeUseCase,
    private val remoteNotificationUnsubscribeUseCase: RemoteNotificationUnsubscribeUseCase
) {
    suspend fun resubscribe() {
        NotificationResultsAvailable.entries.forEach {
            when (notificationRepository.isEnabled(it)) {
                true -> remoteNotificationSubscribeUseCase.subscribe(it.remoteSubscriptionTopic)
                false -> remoteNotificationUnsubscribeUseCase.unsubscribe(it.remoteSubscriptionTopic)
            }
        }
    }
}