package tmg.flashback.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import com.google.firebase.messaging.BuildConfig
import com.google.firebase.messaging.FirebaseMessaging
import tmg.flashback.R
import tmg.flashback.repo.enums.NotificationRegistration.OPT_IN
import tmg.flashback.repo.enums.NotificationRegistration.OPT_OUT
import tmg.flashback.repo.pref.PrefsDB
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebasePushNotificationManager(
    private val applicationContext: Context,
    private val pref: PrefsDB
): PushNotificationManager {

    companion object {
        const val topicRace: String = "race"
        const val topicQualifying: String = "qualifying"
    }


    override suspend fun raceSubscribe(): Boolean {
        return suspendCoroutine { continuation ->
            FirebaseMessaging
                .getInstance()
                .subscribeToTopic(topicRace)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        pref.notificationsRace = OPT_IN
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
                        pref.notificationsRace = OPT_OUT
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
                        pref.notificationsQualifying = OPT_IN
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
                        pref.notificationsQualifying = OPT_OUT
                    }
                    continuation.resume(it.isSuccessful)
                }
        }
    }

    override fun createChannels() {
        createChannel(topicRace, R.string.notification_channel_race)
        createChannel(topicQualifying, R.string.notification_channel_qualifying)
    }

    private fun createChannel(channelId: String, @StringRes channelName: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, applicationContext.getString(channelName), NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}