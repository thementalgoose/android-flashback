package tmg.flashback.crash_reporting.repository

import tmg.flashback.prefs.manager.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrashRepository @Inject constructor(
    private val preferenceManager: PreferenceManager
) {
    companion object {
        private const val keyCrashReporting: String = "CRASH_REPORTING"
        private const val keyShakeToReport: String = "SHAKE_TO_REPORT"
    }

    var isEnabled: Boolean
        get() = preferenceManager.getBoolean(keyCrashReporting, true)
        set(value) = preferenceManager.save(keyCrashReporting, value)

    var shakeToReport: Boolean
        get() = preferenceManager.getBoolean(keyShakeToReport, true)
        set(value) = preferenceManager.save(keyShakeToReport, value)
}