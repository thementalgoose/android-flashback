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
    var enabled: Boolean
        get() = crashRepository.isEnabled
        set(value) {
            crashRepository.isEnabled = value
        }

    var shakeToReport: Boolean
        get() = crashRepository.shakeToReport
        set(value) {
            crashRepository.shakeToReport = value
        }

    fun initialise(
        deviceUdid: String,
        appOpenedCount: Int,
        appFirstOpened: LocalDate
    ) {
        crashService.initialise(
            enableCrashReporting = crashRepository.isEnabled,
            deviceUdid = deviceUdid, // deviceRepository.deviceUdid,
            appOpenedCount = appOpenedCount, // deviceRepository.appOpenedCount,
            appFirstOpened = appFirstOpened.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.UK)) // // deviceRepository.appFirstOpened
        )
    }

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

    // TODO: Remove this
    fun logError(error: Exception, msg: String?) {
        if (enabled) {
            crashService.logException(error, msg ?: error.message ?: "Error occurred")
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

    fun <T> attempt(msgForException: String? = null, closure: () -> T?) {
        try {
            closure.invoke()
        } catch (e: Exception) {
            logException(e, msgForException)
        }
    }
}