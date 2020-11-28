package tmg.flashback.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject
import tmg.flashback.R
import tmg.flashback.SplashActivity
import tmg.flashback.notifications.FirebasePushNotificationManager.Companion.topicQualifying
import tmg.flashback.notifications.FirebasePushNotificationManager.Companion.topicRace

class RaceNotificationService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let {
            if (it.title != null) {
                sendNotification(
                    channelId = it.channelId ?: "race",
                    title = it.title!!,
                    text = it.body
                )
            }
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        /* Do nothing with a new token */
    }

    /**
     * Register a local notification for when the app is open
     */
    private fun sendNotification(channelId: String, title: String, text: String?) {
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(text ?: "")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel: NotificationChannel? = when (channelId) {
                topicRace -> NotificationChannel(channelId, getString(R.string.notification_channel_race), NotificationManager.IMPORTANCE_DEFAULT)
                topicQualifying -> NotificationChannel(channelId, getString(R.string.notification_channel_qualifying), NotificationManager.IMPORTANCE_DEFAULT)
                else -> null
            }
            channel?.let {
                notificationManager.createNotificationChannel(it)
            }
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

}