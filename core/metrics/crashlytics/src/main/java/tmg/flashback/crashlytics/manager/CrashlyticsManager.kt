package tmg.flashback.crashlytics.manager

import android.util.Log
import tmg.flashback.crashlytics.BuildConfig
import tmg.flashback.crashlytics.services.FirebaseCrashService
import tmg.flashback.device.repository.PrivacyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrashlyticsManager @Inject constructor(
    private val crashRepository: PrivacyRepository,
    private val firebaseCrashService: FirebaseCrashService
) {

    private val enabled
        get() = crashRepository.crashReporting

    fun logError(msg: String) {
        if (enabled) {
            firebaseCrashService.logError(msg)
        }
    }

    fun log(msg: String) {
        if (enabled) {
            firebaseCrashService.logInfo(msg)
        }
    }

    fun logException(error: Exception, msg: String? = null) {
        if (BuildConfig.DEBUG) {
            Log.d("CrashController", "Exception thrown, message $msg")
            error.printStackTrace()
        }
        if (enabled) {
            firebaseCrashService.logException(error, msg ?: error.message ?: "Error occurred")
        }
    }
}