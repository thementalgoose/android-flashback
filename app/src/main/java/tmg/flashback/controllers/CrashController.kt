package tmg.flashback.controllers

import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.firebase.crash.FirebaseCrashManager
import tmg.flashback.repo.pref.DeviceRepository
import java.lang.Exception

/**
 * Handle crash reporting and logging exceptions
 */
class CrashController(
        private val deviceRepository: DeviceRepository,
        private val firebaseCrashManager: FirebaseCrashManager
) {
    var crashReporting: Boolean
        get() = deviceRepository.crashReporting
        set(value) {
            deviceRepository.crashReporting = value
        }

    fun initialiseCrashReporting() {
        firebaseCrashManager.initialise(
                enableCrashReporting = crashReporting,
                deviceUdid = deviceRepository.deviceUdid,
                appFirstOpened = deviceRepository.appFirstBootTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                appOpenedCount = deviceRepository.appOpenedCount
        )
    }

    fun log(msg: String) {
        firebaseCrashManager.log(msg)
    }

    fun logError(error: Exception, msg: String) {
        firebaseCrashManager.logError(error, msg)
    }
}