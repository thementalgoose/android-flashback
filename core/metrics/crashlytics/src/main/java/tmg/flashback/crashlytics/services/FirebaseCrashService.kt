package tmg.flashback.crashlytics.services

/**
 * Wrapper around the Firebase Crashlytics that the app uses to report crashes
 * Abstracted for testing
 */
interface FirebaseCrashService {
    fun initialise(
        enableCrashReporting: Boolean,
        deviceUdid: String,
        appFirstOpened: String,
        appOpenedCount: Int
    )
    fun logInfo(msg: String)
    fun logError(msg: String)
    fun logException(error: Exception, context: String)
}