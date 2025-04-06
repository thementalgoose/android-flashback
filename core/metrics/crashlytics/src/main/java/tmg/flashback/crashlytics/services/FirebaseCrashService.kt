package tmg.flashback.crashlytics.services

import tmg.flashback.crashlytics.model.FirebaseKey

/**
 * Wrapper around the Firebase Crashlytics that the app uses to report crashes
 * Abstracted for testing
 */
interface FirebaseCrashService {
    fun setCrashlyticsCollectionEnabled(enabled: Boolean)
    fun setCustomKey(key: FirebaseKey, value: String)
    fun setCustomKey(key: FirebaseKey, value: Boolean)
    fun setUserId(userId: String)
    fun logInfo(msg: String)
    fun logError(msg: String)
    fun logException(error: Exception, context: String)
}