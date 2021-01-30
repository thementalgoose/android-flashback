package tmg.flashback.core.controllers

import tmg.flashback.core.managers.CrashManager
import tmg.flashback.core.repositories.CoreRepository
import java.lang.Exception

class CrashController(
    private val coreRepository: CoreRepository,
    private val crashManager: CrashManager
) {
    var enabled: Boolean
        get() = coreRepository.crashReporting
        set(value) {
            coreRepository.crashReporting = value
        }

    fun initialise() {
        crashManager.initialise(
            enableCrashReporting = coreRepository.crashReporting,
            enableAnalytics = coreRepository.analytics,
            deviceUdid = coreRepository.deviceUdid,
            appOpenedCount = coreRepository.appOpenedCount,
            appFirstOpened = coreRepository.appFirstBootTime.toString()
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