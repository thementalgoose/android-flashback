package tmg.notifications.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.lang.NullPointerException
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tmg.crash_reporting.controllers.CrashController
import tmg.notifications.BuildConfig
import tmg.notifications.managers.SystemNotificationManager

@KoinApiExtension
class LocalNotificationBroadcastReceiver: BroadcastReceiver(), KoinComponent {

    private val notificationManager: SystemNotificationManager by inject()

    private val crashController: CrashController by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        println("RECEIVED!")

        if (BuildConfig.DEBUG) {
            Log.d("Flashback", "Local notification broadcast receiver fired")
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
            Log.d("Flashback", "Scheduling notification for Title $title Description $description")
        }

        println("Notification controller $notificationManager")

        val notification = notificationManager.buildNotification(
            context = context,
            channelId = channelId,
            title = title,
            text = description
        )

        Log.i("Flashback", "Displaying notification for $title")

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
