package tmg.flashback.stats.usecases

import tmg.flashback.notifications.usecases.RemoteNotificationSubscribeUseCase
import tmg.flashback.notifications.usecases.RemoteNotificationUnsubscribeUseCase
import tmg.flashback.stats.repository.NotificationRepository

class ResubscribeNotificationsUseCase(
    private val notificationRepository: NotificationRepository,
    private val remoteNotificationSubscribeUseCase: RemoteNotificationSubscribeUseCase,
    private val remoteNotificationUnsubscribeUseCase: RemoteNotificationUnsubscribeUseCase
) {
    suspend fun resubscribe() {
        when (notificationRepository.notificationNotifyRace) {
            true -> remoteNotificationSubscribeUseCase.subscribe("notify_race")
            false -> remoteNotificationUnsubscribeUseCase.unsubscribe("notify_race")
        }
        when (notificationRepository.notificationNotifySprint) {
            true -> remoteNotificationSubscribeUseCase.subscribe("notify_sprint")
            false -> remoteNotificationUnsubscribeUseCase.unsubscribe("notify_sprint")
        }
        when (notificationRepository.notificationNotifyQualifying) {
            true -> remoteNotificationSubscribeUseCase.subscribe("notify_qualifying")
            false -> remoteNotificationUnsubscribeUseCase.unsubscribe("notify_qualifying")
        }
    }
}