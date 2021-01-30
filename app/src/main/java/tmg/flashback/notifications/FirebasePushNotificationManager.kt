package tmg.flashback.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import com.google.firebase.messaging.FirebaseMessaging
import tmg.flashback.R
import tmg.flashback.controllers.NotificationController
import tmg.flashback.data.enums.NotificationRegistration.OPT_IN
import tmg.flashback.data.enums.NotificationRegistration.OPT_OUT
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebasePushNotificationManager(
    private val applicationContext: Context,
    private val notificationController: NotificationController
): PushNotificationManager {

    companion object {
        const val topicRace: String = "race"
        const val topicQualifying: String = "qualifying"
        const val topicMisc: String = "misc"
    }

    override suspend fun raceSubscribe(): Boolean {
        return suspendCoroutine { continuation ->
            FirebaseMessaging
                .getInstance()
                .subscribeToTopic(topicRace)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        notificationController.raceOptIn = true
                    }
                    continuation.resume(it.isSuccessful)
                }
        }
    }

    override suspend fun raceUnsubscribe(): Boolean {
        return suspendCoroutine { continuation ->
            FirebaseMessaging
                .getInstance()
                .unsubscribeFromTopic(topicRace)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        notificationController.raceOptIn = false
                    }
                    continuation.resume(it.isSuccessful)
                }
        }
    }

    override suspend fun qualifyingSubscribe(): Boolean {
        return suspendCoroutine { continuation ->
            FirebaseMessaging
                .getInstance()
                .subscribeToTopic(topicQualifying)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        notificationController.qualifyingOptIn = true
                    }
                    continuation.resume(it.isSuccessful)
                }
        }
    }

    override suspend fun qualifyingUnsubscribe(): Boolean {
        return suspendCoroutine { continuation ->
            FirebaseMessaging
                .getInstance()
                .unsubscribeFromTopic(topicQualifying)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        notificationController.qualifyingOptIn = false
                    }
                    continuation.resume(it.isSuccessful)
                }
        }
    }

    override suspend fun appSupportSubscribe(): Boolean {
        return suspendCoroutine { continuation ->
            FirebaseMessaging
                .getInstance()
                .unsubscribeFromTopic(topicMisc)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        notificationController.miscOptIn = true
                    }
                    continuation.resume(it.isSuccessful)
                }
        }
    }

    override suspend fun appSupportUnsubscribe(): Boolean {
        return suspendCoroutine { continuation ->
            FirebaseMessaging
                .getInstance()
                .unsubscribeFromTopic(topicMisc)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        notificationController.miscOptIn = false
                    }
                    continuation.resume(it.isSuccessful)
                }
        }
    }

    override fun createChannels() {
        createChannel(topicRace, R.string.notification_channel_race)
        createChannel(topicQualifying, R.string.notification_channel_qualifying)
        createChannel(topicMisc, R.string.notification_channel_info)
    }

    private fun createChannel(channelId: String, @StringRes channelName: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, applicationContext.getString(channelName), NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}