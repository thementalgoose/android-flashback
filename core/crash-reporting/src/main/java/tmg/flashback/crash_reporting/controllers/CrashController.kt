package tmg.flashback.crash_reporting.controllers

import android.util.Log
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.crash_reporting.BuildConfig
import tmg.flashback.crash_reporting.repository.CrashRepository
import tmg.flashback.crash_reporting.services.CrashService
import java.util.*

class CrashController(
    private val crashRepository: CrashRepository,
    private val crashService: CrashService
) {

    private val enabled get() = crashRepository.isEnabled

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