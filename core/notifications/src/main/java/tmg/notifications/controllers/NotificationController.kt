package tmg.notifications.controllers

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.StringRes
import org.threeten.bp.LocalDateTime
import tmg.notifications.BuildConfig
import tmg.notifications.NotificationRegistration
import tmg.notifications.R
import tmg.notifications.managers.RemoteNotificationManager
import tmg.notifications.managers.SystemAlarmManager
import tmg.notifications.managers.SystemNotificationManager
import tmg.notifications.repository.NotificationRepository

class NotificationController(
    private val notificationRepository: NotificationRepository,
    private val localNotificationManager: SystemNotificationManager,
    private val remoteNotificationManager: RemoteNotificationManager,
    private val alarmManager: SystemAlarmManager
) {

    companion object {
        const val channelIdOther: String = "seasonInfo"
    }

    /**
     * Notifications channels supported
     */
    val isNotificationChannelsSupported: Boolean
        @SuppressLint("AnnotateVersionCheck")
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    /**
     * Schedule a local notification
     */
    fun scheduleLocalNotification(
        requestCode: Int,
        channelId: String,
        title: String,
        text: String,
        timestamp: LocalDateTime
    ) {
        alarmManager.schedule(requestCode, channelId, title, text, timestamp)
        notificationRepository.notificationIds = notificationRepository.notificationIds
            .toMutableSet()
            .apply { add(requestCode) }

        if (BuildConfig.DEBUG) {
            Log.d("Flashback", "Scheduled notification $title / $text - $requestCode")
        }
    }

    /**
     * Cancel a specific notification
     */
    fun cancelLocalNotification(requestCode: Int) {
        alarmManager.cancel(requestCode)
        notificationRepository.notificationIds = notificationRepository.notificationIds
            .toMutableSet()
            .apply {
                remove(requestCode)
            }

        if (BuildConfig.DEBUG) {
            Log.d("Flashback", "Notification - Cancelled $requestCode, leaving ${notificationRepository.notificationIds}")
        }
    }

    /**
     * Cancel all local notifications that have been scheduled
     */
    fun cancelAllNotifications() {
        if (BuildConfig.DEBUG) {
            Log.d("Flashback", "Notification - Cancelling all (all = ${notificationRepository.notificationIds}")
        }
        notificationRepository.notificationIds
            .forEach { requestCode ->
                alarmManager.cancel(requestCode)
            }
        notificationRepository.notificationIds = emptySet()
    }

    val numberOfScheduledNotifications: Int
        get() = notificationRepository.notificationIds.size

    /**
     * Subscribe to receive notifications from these topics
     */
    // TODO: To be removed / trimmed down to only default
    suspend fun subscribeToRemoteNotifications(): Boolean {

        remoteNotificationManager.unsubscribeToTopic("race")
        remoteNotificationManager.unsubscribeToTopic("qualifying")

        if (notificationRepository.enabledSeasonInfo == NotificationRegistration.DEFAULT) {
            val result = remoteNotificationManager.subscribeToTopic(channelIdOther)
            if (result) {
                notificationRepository.enabledSeasonInfo = NotificationRegistration.OPT_IN
            }
        }

        return true
    }

    /**
     * Create notification channels for the device
     */
    fun createNotificationChannel(channelId: String, @StringRes label: Int) {
        localNotificationManager.createChannel(channelId, label)
    }

    /**
     * Delete a notification channel
     */
    fun deleteNotificationChannel(channelId: String) {
        localNotificationManager.cancelChannel(channelId)
    }
}