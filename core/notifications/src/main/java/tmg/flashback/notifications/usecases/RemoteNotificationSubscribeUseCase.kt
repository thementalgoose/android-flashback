package tmg.flashback.notifications.usecases

import tmg.flashback.notifications.managers.RemoteNotificationManager
import tmg.flashback.notifications.repository.NotificationRepository
import javax.inject.Inject

class RemoteNotificationSubscribeUseCase @Inject constructor(
    private val remoteNotificationManager: RemoteNotificationManager,
    private val notificationRepository: NotificationRepository
) {
    suspend fun subscribe(topic: String): Boolean {
        val subscribed = remoteNotificationManager.subscribeToTopic(topic)
        if (!subscribed){
            return false
        }
        val topics = notificationRepository
            .remoteNotificationTopics
            .toMutableSet()
            .apply {
                add(topic)
            }
        notificationRepository.remoteNotificationTopics = topics
        return true
    }
}