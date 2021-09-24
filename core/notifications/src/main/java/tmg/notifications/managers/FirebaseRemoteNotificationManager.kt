package tmg.notifications.managers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class FirebaseRemoteNotificationManager(
    private val applicationContext: Context
): RemoteNotificationManager {

    companion object {
        const val topicRace: String = "race"
        const val topicQualifying: String = "qualifying"
        const val topicSeasonInfo: String = "seasonInfo"
    }

    override suspend fun subscribeToTopic(topic: String): Boolean {
        return suspendCoroutine { continuation ->
            FirebaseMessaging.getInstance()
                .subscribeToTopic(topic)
                .addOnCompleteListener {
                    continuation.resume(it.isSuccessful)
                }
        }
    }

    override suspend fun unsubscribeToTopic(topic: String): Boolean {
        return suspendCoroutine { continuation ->
            FirebaseMessaging.getInstance()
                .unsubscribeFromTopic(topic)
                .addOnCompleteListener {
                    continuation.resume(it.isSuccessful)
                }
        }
    }

    // TODO: Move this to SystemNotificationManager
    override fun createChannel(channelId: String, @StringRes channelName: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                applicationContext.getString(channelName),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}