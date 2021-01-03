package tmg.flashback.firebase.crash

interface FirebaseCrashManager {
    fun initialise(
            enableCrashReporting: Boolean,
            deviceUdid: String,
            appFirstOpened: String,
            appOpenedCount: Int
    )
    fun log(msg: String)
    fun logError(error: Exception, context: String)
}