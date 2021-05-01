package tmg.analytics.repository

import tmg.flashback.device.repository.SharedPreferenceRepository

class AnalyticsRepository(
    private val sharedPreferenceRepository: SharedPreferenceRepository
) {

    companion object {
        private const val keyAnalytics: String = "ANALYTICS_OPT_IN"
        private const val keyAnalyticsDefault: Boolean = true
    }

    var isEnabled: Boolean
        get() = sharedPreferenceRepository.getBoolean(keyAnalytics, keyAnalyticsDefault)
        set(value) = sharedPreferenceRepository.save(keyAnalytics, value)
}