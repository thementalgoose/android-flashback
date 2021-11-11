package tmg.flashback.notifications.managers

import androidx.annotation.StringRes

interface RemoteNotificationManager {

    suspend fun subscribeToTopic(topic: String): Boolean
    suspend fun unsubscribeToTopic(topic: String): Boolean

    fun createChannel(channelId: String, @StringRes channelName: Int)
}