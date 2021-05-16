package tmg.notifications.managers

import androidx.annotation.StringRes

interface PushNotificationManager {

    suspend fun subscribeToTopic(topic: String): Boolean
    suspend fun unsubscribeToTopic(topic: String): Boolean

    fun createChannel(channelId: String, @StringRes channelName: Int)
}