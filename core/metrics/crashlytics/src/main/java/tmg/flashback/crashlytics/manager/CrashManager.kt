package tmg.flashback.crashlytics.manager

import android.util.Log
import tmg.flashback.crashlytics.BuildConfig
import tmg.flashback.crashlytics.services.CrashService
import tmg.flashback.device.repository.PrivacyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrashManager @Inject constructor(
    private val crashRepository: PrivacyRepository,
    private val crashService: CrashService
) {

    private val enabled
        get() = crashRepository.crashReporting

    fun logError(msg: String) {
        if (enabled) {
            crashService.logError(msg)
        }
    }

    fun log(msg: String) {
        if (enabled) {
            crashService.logInfo(msg)
        }
    }

    fun logException(error: Exception, msg: String? = null) {
        if (BuildConfig.DEBUG) {
            Log.d("CrashController", "Exception thrown, message $msg")
            error.printStackTrace()
        }
        if (enabled) {
            crashService.logException(error, msg ?: error.message ?: "Error occurred")
        }
    }
}