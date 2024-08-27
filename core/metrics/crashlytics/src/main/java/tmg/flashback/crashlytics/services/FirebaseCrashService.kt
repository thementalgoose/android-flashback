package tmg.flashback.crashlytics.services

import tmg.flashback.crashlytics.model.FirebaseKey

/**
 * Wrapper around the Firebase Crashlytics that the app uses to report crashes
 * Abstracted for testing
 */
interface FirebaseCrashService {
    fun initialise(
        enableCrashReporting: Boolean,
        deviceUuid: String,
        extraKeys: Map<FirebaseKey, String>
    )
    fun logInfo(msg: String)
    fun logError(msg: String)
    fun logException(error: Exception, context: String)
}