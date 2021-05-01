package tmg.analytics.controllers

import android.os.Bundle
import tmg.analytics.UserProperty
import tmg.analytics.managers.AnalyticsManager
import tmg.analytics.repository.AnalyticsRepository

class AnalyticsController(
    private val analyticsRepository: AnalyticsRepository,
    private val analyticsManager: AnalyticsManager
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
                analyticsManager.logEvent(key, bundle)
            }
            else {
                analyticsManager.logEvent(key)
            }
        }
    }

    fun setUserProperty(property: UserProperty, value: String) {
        if (enabled) {
            analyticsManager.setProperty(property.key, value)
        }
    }

    fun viewScreen(screenName: String, clazz: Class<*>, params: Map<String, String>) {
        if (enabled) {
            analyticsManager.logViewScreen(screenName, clazz, params)
        }
    }
}