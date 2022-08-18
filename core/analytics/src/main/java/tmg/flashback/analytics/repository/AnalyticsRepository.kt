package tmg.flashback.analytics.repository

import tmg.flashback.prefs.manager.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepository @Inject constructor(
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