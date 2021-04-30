package tmg.analytics.repository

import tmg.flashback.device.repository.SharedPreferenceRepository

class AnalyticsRepository(
    private val sharedPrefManager: SharedPreferenceRepository
) {

    companion object {
        private const val keyIsAnalyticsEnabled: String = "ANALYTICS_OPT_IN"
        private const val keyIsAnalyticsEnabledDefaultValue: Boolean = true
    }

    var isAnalyticsEnabled: Boolean
        get() = sharedPrefManager.getBoolean(keyIsAnalyticsEnabled, keyIsAnalyticsEnabledDefaultValue)
        set(value) = sharedPrefManager.save(keyIsAnalyticsEnabled, value)
}