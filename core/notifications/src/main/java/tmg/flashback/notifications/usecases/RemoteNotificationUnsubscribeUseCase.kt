package tmg.flashback.notifications.usecases

import tmg.flashback.notifications.managers.RemoteNotificationManager
import tmg.flashback.notifications.repository.NotificationIdsRepository
import javax.inject.Inject

class RemoteNotificationUnsubscribeUseCase @Inject constructor(
    private val remoteNotificationManager: RemoteNotificationManager,
    private val notificationIdsRepository: NotificationIdsRepository
) {
    suspend fun unsubscribe(topic: String): Boolean {
        val unsubscribed = remoteNotificationManager.unsubscribeToTopic(topic)
        if (!unsubscribed){
            return false
        }

        val topics = notificationIdsRepository
            .remoteNotificationTopics
            .toMutableSet()
            .apply {
                remove(topic)
            }
        notificationIdsRepository.remoteNotificationTopics = topics
        return true
    }
}