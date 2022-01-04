package tmg.flashback.notifications.managers

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import tmg.flashback.notifications.BuildConfig
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class FirebaseRemoteNotificationManager: RemoteNotificationManager {

    override suspend fun subscribeToTopic(topic: String): Boolean {
        return suspendCoroutine { continuation ->
            FirebaseMessaging.getInstance()
                .subscribeToTopic(topic)
                .addOnCompleteListener {
                    if (BuildConfig.DEBUG) {
                        Log.i("Notifications", "Remote Topic $topic successfully subscribed ${it.isSuccessful} ${it.exception?.message}")
                        it.exception?.printStackTrace()
                    }
                    continuation.resume(it.isSuccessful)
                }
        }
    }

    override suspend fun unsubscribeToTopic(topic: String): Boolean {
        return suspendCoroutine { continuation ->
            FirebaseMessaging.getInstance()
                .unsubscribeFromTopic(topic)
                .addOnCompleteListener {
                    if (BuildConfig.DEBUG) {
                        Log.i("Notifications", "Remote Topic $topic successfully unsubscribed ${it.isSuccessful} ${it.exception?.message}")
                        it.exception?.printStackTrace()
                    }
                    continuation.resume(it.isSuccessful)
                }
        }
    }
}