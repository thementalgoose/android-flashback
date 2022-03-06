package tmg.flashback.notifications.usecases

import tmg.flashback.notifications.managers.RemoteNotificationManager
import tmg.flashback.notifications.repository.NotificationRepository

class RemoteNotificationUnsubscribeUseCase(
    private val remoteNotificationManager: RemoteNotificationManager,
    private val notificationRepository: NotificationRepository
) {
    suspend fun unsubscribe(topic: String): Boolean {
        val unsubscribed = remoteNotificationManager.unsubscribeToTopic(topic)
        if (!unsubscribed){
            return false
        }

        val topics = notificationRepository
            .remoteNotificationTopics
            .toMutableSet()
            .apply {
                remove(topic)
            }
        notificationRepository.remoteNotificationTopics = topics
        return true
    }
}