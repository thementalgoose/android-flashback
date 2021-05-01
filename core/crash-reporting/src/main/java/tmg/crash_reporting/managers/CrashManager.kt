package tmg.crash_reporting.managers

/**
 * Wrapper around the Firebase Crashlytics that the app uses to report crashes
 * Abstracted for testing
 */
interface CrashManager {
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