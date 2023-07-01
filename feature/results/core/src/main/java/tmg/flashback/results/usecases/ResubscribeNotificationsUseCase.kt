package tmg.flashback.results.usecases

import tmg.flashback.notifications.usecases.RemoteNotificationSubscribeUseCase
import tmg.flashback.notifications.usecases.RemoteNotificationUnsubscribeUseCase
import tmg.flashback.results.contract.repository.models.NotificationResultsAvailable
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import javax.inject.Inject

class ResubscribeNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationsRepositoryImpl,
    private val remoteNotificationSubscribeUseCase: RemoteNotificationSubscribeUseCase,
    private val remoteNotificationUnsubscribeUseCase: RemoteNotificationUnsubscribeUseCase
) {
    suspend fun resubscribe() {
        NotificationResultsAvailable.values().forEach {
            when (notificationRepository.isEnabled(it)) {
                true -> remoteNotificationSubscribeUseCase.subscribe(it.remoteSubscriptionTopic)
                false -> remoteNotificationUnsubscribeUseCase.unsubscribe(it.remoteSubscriptionTopic)
            }
        }
    }
}