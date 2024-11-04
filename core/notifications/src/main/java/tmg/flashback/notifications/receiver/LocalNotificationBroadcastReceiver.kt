package tmg.flashback.notifications.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.notifications.BuildConfig
import tmg.flashback.notifications.managers.SystemNotificationManager
import tmg.flashback.notifications.model.NotificationPriority
import javax.inject.Inject

@AndroidEntryPoint
class LocalNotificationBroadcastReceiver @Inject constructor(): BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: SystemNotificationManager

    @Inject
    lateinit var crashController: CrashlyticsManager

    override fun onReceive(context: Context?, intent: Intent?) {
        if (BuildConfig.DEBUG) {
            Log.d("Notification", "Local notification broadcast receiver fired")
        }

        val title = intent?.extras?.getString(keyTitle) ?: run {
            crashController.logException(NullPointerException("title null when trying to show notification"), "title null when trying to show notification")
            return
        }
        val description = intent.extras?.getString(keyDescription) ?: run {
            crashController.logException(NullPointerException("description null when trying to show notification"), "description null when trying to show notification")
            return
        }
        val channelId = intent.extras?.getString(keyChannelId) ?: run {
            crashController.logException(NullPointerException("Channel id null when trying to show notification"), "Channel id null when trying to show notification")
            return
        }
        if (BuildConfig.DEBUG) {
            Log.d("Notification", "Scheduling notification for Title '$title' Description '$description'")
        }

        val notification = notificationManager.buildNotification(
            context = context,
            channelId = channelId,
            title = title,
            text = description,
            priority = when (channelId) {
                // NotificationChannel in :feature:stats module
                "flashback_sprint" -> NotificationPriority.HIGH
                "flashback_sprint_qualifying" -> NotificationPriority.HIGH
                "flashback_qualifying" -> NotificationPriority.HIGH
                "flashback_race" -> NotificationPriority.HIGH
                else -> NotificationPriority.DEFAULT
            }
        )

        Log.i("Notification", "Displaying notification for '$title'")

        notificationManager.notify("flashback", 1001, notification)
    }

    companion object {

        private const val keyTitle: String = "keyTitle"
        private const val keyChannelId: String = "keyChannelId"
        private const val keyDescription: String = "keyDescription"

        fun intent(context: Context, channelId: String, title: String, description: String): Intent {
            return Intent(context, LocalNotificationBroadcastReceiver::class.java).apply {
                putExtra(keyChannelId, channelId)
                putExtra(keyTitle, title)
                putExtra(keyDescription, description)
            }
        }
    }
}
