package tmg.flashback.device.repository

import tmg.flashback.prefs.manager.PreferenceManager
import javax.inject.Inject

class PrivacyRepository @Inject constructor(
    private val preferenceManager: PreferenceManager
) {

    companion object {
        private const val keyAnalytics: String = "ANALYTICS_OPT_IN"
        private const val keyCrashReporting: String = "CRASH_REPORTING"
        private const val keyShakeToReport: String = "SHAKE_TO_REPORT"
        private const val keyAnalyticsDefault: Boolean = true
    }

    var crashReporting: Boolean
        get() = preferenceManager.getBoolean(keyCrashReporting, true)
        set(value) = preferenceManager.save(keyCrashReporting, value)

    var shakeToReport: Boolean
        get() = preferenceManager.getBoolean(keyShakeToReport, true)
        set(value) = preferenceManager.save(keyShakeToReport, value)

    var analytics: Boolean
        get() = preferenceManager.getBoolean(keyAnalytics, keyAnalyticsDefault)
        set(value) = preferenceManager.save(keyAnalytics, value)
}