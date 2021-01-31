package tmg.flashback.core.controllers

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

    fun setUserProperty(property: UserProperty, value: String) {
        if (BuildConfig.DEBUG) {
            Log.i("Flashback", "User property $property -> $value ($enabled)")
        }
        if (enabled) {
            analyticsManager.setProperty(property.key, value)
        }
    }

    fun viewScreen(screenName: String, clazz: Class<*>, params: Map<String, String>) {
        if (BuildConfig.DEBUG) {
            Log.i("Flashback", "View screen $screenName -> $params ($enabled)")
        }
        if (enabled) {
            analyticsManager.logViewScreen(screenName, clazz, params)
        }
    }

}
