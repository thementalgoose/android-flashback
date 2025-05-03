package tmg.flashback.notifications.usecases

import tmg.flashback.notifications.managers.RemoteNotificationManager
import tmg.flashback.notifications.repository.NotificationIdsRepository
import javax.inject.Inject

class RemoteNotificationSubscribeUseCase @Inject constructor(
    private val remoteNotificationManager: RemoteNotificationManager,
    private val notificationIdsRepository: NotificationIdsRepository
) {
    suspend fun subscribe(topic: String): Boolean {
        val subscribed = remoteNotificationManager.subscribeToTopic(topic)
        if (!subscribed){
            return false
        }
        val topics = notificationIdsRepository
            .remoteNotificationTopics
            .toMutableSet()
            .apply {
                add(topic)
            }
        notificationIdsRepository.remoteNotificationTopics = topics
        return true
    }
}