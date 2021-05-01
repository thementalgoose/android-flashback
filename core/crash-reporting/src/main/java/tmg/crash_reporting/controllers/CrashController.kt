package tmg.crash_reporting.controllers

import org.threeten.bp.format.DateTimeFormatter
import tmg.crash_reporting.managers.CrashManager
import tmg.crash_reporting.repository.CrashRepository
import tmg.flashback.device.controllers.DeviceController
import tmg.flashback.device.repository.DeviceRepository
import java.lang.Exception

class CrashController(
    private val crashRepository: CrashRepository,
    private val deviceRepository: DeviceRepository,
    private val crashManager: CrashManager
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

    fun initialise() {
        crashManager.initialise(
            enableCrashReporting = crashRepository.isEnabled,
            deviceUdid = deviceRepository.deviceUdid,
            appOpenedCount = deviceRepository.appOpenedCount,
            appFirstOpened = deviceRepository.appFirstOpened.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        )
    }

    fun logError(msg: String) {
        if (enabled) {
            crashManager.logError(msg)
        }
    }

    fun log(msg: String) {
        if (enabled) {
            crashManager.logInfo(msg)
        }
    }

    fun logError(error: Exception, msg: String) {
        if (enabled) {
            crashManager.logException(error, msg)
        }
    }
}