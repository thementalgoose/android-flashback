package tmg.flashback.analytics.manager

import android.os.Bundle
import tmg.flashback.analytics.UserProperty
import tmg.flashback.analytics.repository.AnalyticsRepository
import tmg.flashback.analytics.services.AnalyticsService

class AnalyticsManager(
    private val analyticsRepository: AnalyticsRepository,
    private val analyticsService: AnalyticsService
) {
    var enabled: Boolean
        get() = analyticsRepository.isEnabled
        set(value) {
            analyticsRepository.isEnabled = value
        }

    fun initialise(userId: String) {
        analyticsService.setUserId(userId)
        analyticsService.setAnalyticsCollectionEnabled(enabled)
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