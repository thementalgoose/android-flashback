package tmg.flashback.notifications.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.notifications.BuildConfig
import tmg.flashback.notifications.R
import tmg.flashback.notifications.navigation.NotificationNavigationProvider
import tmg.flashback.notifications.repository.NotificationIdsRepository
import javax.inject.Inject

@AndroidEntryPoint
class RemoteNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var navigationProvider: NotificationNavigationProvider

    @Inject
    lateinit var notificationIdsRepository: NotificationIdsRepository

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let {
            if (it.title != null) {
                sendNotification(
                    channelId = it.channelId ?: "flashback_info",
                    title = it.title!!,
                    text = it.body
                )
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        if (BuildConfig.DEBUG) {
            Log.i("Notifications", "New token for remote push notifications '$token'")
        }
        notificationIdsRepository.remoteNotificationToken = token
        /* Do nothing with a new token */
    }

    /**
     * Register a local notification for when the app is open
     */
    private fun sendNotification(channelId: String, title: String, text: String?) {
        val intent = navigationProvider.relaunchAppIntent(this)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(text ?: "")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}