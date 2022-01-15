package tmg.flashback.notifications.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.notifications.BuildConfig
import tmg.flashback.notifications.managers.SystemNotificationManager

class LocalNotificationBroadcastReceiver: BroadcastReceiver(), KoinComponent {

    private val notificationManager: SystemNotificationManager by inject()

    private val crashController: CrashController by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (BuildConfig.DEBUG) {
            Log.d("Notification", "Local notification broadcast receiver fired")
        }

        val title = intent?.extras?.getString(keyTitle) ?: run {
            crashController.logError(NullPointerException("title null when trying to show notification"), "title null when trying to show notification")
            return
        }
        val description = intent.extras?.getString(keyDescription) ?: run {
            crashController.logError(NullPointerException("description null when trying to show notification"), "description null when trying to show notification")
            return
        }
        val channelId = intent.extras?.getString(keyChannelId) ?: run {
            crashController.logError(NullPointerException("Channel id null when trying to show notification"), "Channel id null when trying to show notification")
            return
        }
        if (BuildConfig.DEBUG) {
            Log.d("Notification", "Scheduling notification for Title $title Description $description")
        }

        val notification = notificationManager.buildNotification(
            context = context,
            channelId = channelId,
            title = title,
            text = description
        )

        Log.i("Notification", "Displaying notification for $title")

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
