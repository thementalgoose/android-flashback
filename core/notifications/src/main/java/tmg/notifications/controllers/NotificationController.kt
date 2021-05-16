package tmg.notifications.controllers

import android.os.Build
import androidx.annotation.StringRes
import tmg.notifications.NotificationRegistration
import tmg.notifications.R
import tmg.notifications.managers.PushNotificationManager
import tmg.notifications.repository.NotificationRepository

class NotificationController(
    private val notificationRepository: NotificationRepository,
    private val notificationManager: PushNotificationManager
) {

    companion object {
        const val keyTopicRace: String = "race"
        const val keyTopicQualifying: String = "qualifying"
        const val keyTopicSeasonInfo: String = "seasonInfo"
    }

    /**
     * Notifications channels supported
     */
    val isNotificationChannelsSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    /**
     * Subscribe to receive notifications from these topics
     */
    suspend fun subscribe(): Boolean {
        if (notificationRepository.enabledRace == NotificationRegistration.DEFAULT) {
            val result = notificationManager.subscribeToTopic(keyTopicRace)
            if (result) {
                notificationRepository.enabledRace = NotificationRegistration.OPT_IN
            }
        }
        if (notificationRepository.enabledQualifying == NotificationRegistration.DEFAULT) {
            val result = notificationManager.subscribeToTopic(keyTopicQualifying)
            if (result) {
                notificationRepository.enabledQualifying = NotificationRegistration.OPT_IN
            }
        }
        if (notificationRepository.enabledSeasonInfo == NotificationRegistration.DEFAULT) {
            val result = notificationManager.subscribeToTopic(keyTopicSeasonInfo)
            if (result) {
                notificationRepository.enabledSeasonInfo = NotificationRegistration.OPT_IN
            }
        }
        return true
    }

    /**
     * Create notification channels for the device
     */
    fun createNotificationChannels() {
        notificationManager.createChannel(keyTopicRace, R.string.notification_channel_race)
        notificationManager.createChannel(keyTopicQualifying, R.string.notification_channel_qualifying)
        notificationManager.createChannel(keyTopicSeasonInfo, R.string.notification_channel_info)
    }
}