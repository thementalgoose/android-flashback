package tmg.crash_reporting.controllers

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.crash_reporting.services.CrashService
import tmg.crash_reporting.repository.CrashRepository
import java.lang.Exception

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
            appFirstOpened = appFirstOpened.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) // // deviceRepository.appFirstOpened
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

    fun logError(error: Exception, msg: String) {
        if (enabled) {
            crashService.logException(error, msg)
        }
    }
}