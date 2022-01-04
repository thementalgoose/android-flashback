package tmg.flashback.notifications.managers

interface RemoteNotificationManager {

    suspend fun subscribeToTopic(topic: String): Boolean
    suspend fun unsubscribeToTopic(topic: String): Boolean
}