package tmg.core.analytics.repository

import tmg.core.prefs.manager.PreferenceManager

class AnalyticsRepository(
    private val preferenceManager: PreferenceManager
) {

    companion object {
        private const val keyAnalytics: String = "ANALYTICS_OPT_IN"
        private const val keyAnalyticsDefault: Boolean = true
    }

    var isEnabled: Boolean
        get() = preferenceManager.getBoolean(keyAnalytics, keyAnalyticsDefault)
        set(value) = preferenceManager.save(keyAnalytics, value)
}