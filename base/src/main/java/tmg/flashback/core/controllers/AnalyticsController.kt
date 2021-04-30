package tmg.flashback.core.controllers

import android.os.Bundle
import android.util.Log
import tmg.flashback.core.BuildConfig
import tmg.flashback.core.enums.UserProperty
import tmg.flashback.core.managers.AnalyticsManager
import tmg.flashback.core.repositories.CoreRepository

class AnalyticsController(
    private val coreRepository: CoreRepository,
    private val analyticsManager: AnalyticsManager
) {
    var enabled: Boolean
        get() = coreRepository.analytics
        set(value) {
            coreRepository.analytics = value
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
