package tmg.core.analytics.manager

import android.os.Bundle
import tmg.core.analytics.UserProperty
import tmg.core.analytics.services.AnalyticsService
import tmg.core.analytics.repository.AnalyticsRepository

class AnalyticsManager(
    private val analyticsRepository: AnalyticsRepository,
    private val analyticsService: AnalyticsService
) {
    var enabled: Boolean
        get() = analyticsRepository.isEnabled
        set(value) {
            analyticsRepository.isEnabled = value
        }

    fun logEvent(key: String, params: Map<String, String> = emptyMap()) {
        if (enabled) {
            if (params.isNotEmpty()) {
                val bundle = Bundle().apply {
                    for (x in params) {
                        putString(x.key, x.value)
                    }
                }
                analyticsService.logEvent(key, bundle)
            }
            else {
                analyticsService.logEvent(key)
            }
        }
    }

    fun setUserProperty(property: UserProperty, value: String) {
        if (enabled) {
            analyticsService.setProperty(property.key, value)
        }
    }

    fun viewScreen(screenName: String, clazz: Class<*>, params: Map<String, String>) {
        if (enabled) {
            analyticsService.logViewScreen(screenName, clazz, params)
        }
    }
}