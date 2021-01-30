package tmg.flashback.firebase.crash

interface FirebaseCrashManager {
    fun initialise(
            enableCrashReporting: Boolean,
            enableAnalytics: Boolean,
            deviceUdid: String,
            appFirstOpened: String,
            appOpenedCount: Int
    )
    fun logInfo(msg: String)
    fun logError(msg: String)
    fun logException(error: Exception, context: String)
}