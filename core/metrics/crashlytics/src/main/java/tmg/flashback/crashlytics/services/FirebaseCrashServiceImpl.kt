package tmg.flashback.crashlytics.services

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import tmg.flashback.crashlytics.BuildConfig
import tmg.flashback.crashlytics.model.FirebaseKey
import javax.inject.Inject

internal class FirebaseCrashServiceImpl @Inject constructor(): FirebaseCrashService {

    companion object {
        private const val TAG = "Crashlytics"
    }

    private val instance: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    override fun setCrashlyticsCollectionEnabled(enabled: Boolean) {
        instance.isCrashlyticsCollectionEnabled = enabled
    }

    override fun setCustomKey(key: FirebaseKey, value: String) {
        instance.setCustomKey(key, value)
    }

    override fun setCustomKey(key: FirebaseKey, value: Boolean) {
        instance.setCustomKey(key, value)
    }

    override fun setUserId(userId: String) {
        instance.setUserId(userId)
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "User id set to '$userId'")
        }
    }

    override fun logError(msg: String) {
        instance.log(msg)
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "Log error '$msg'")
        }
    }

    override fun logInfo(msg: String) {
        instance.log(msg)
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Log info '$msg'")
        }
    }

    override fun logException(error: Exception, context: String) {
        instance.log(error.message ?: "Exception error $error")
        instance.recordException(error)
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "Log Exception '${error.message}'")
            error.printStackTrace()
        }
    }

    private fun FirebaseCrashlytics.setCustomKey(key: FirebaseKey, value: Boolean) =
        setCustomKey(key, if (value) "true" else "false")

    private fun FirebaseCrashlytics.setCustomKey(key: FirebaseKey, value: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Custom Key: ${key.label} = $value")
        }
        this.setCustomKey(key.label, value)
    }
}