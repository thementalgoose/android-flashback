package tmg.flashback.core.managers

import android.os.Bundle

interface AnalyticsManager {

    fun setProperty(key: String, value: String)

    fun logEvent(key: String, bundle: Bundle)

    fun logViewScreen(screenName: String, clazz: Class<*>, mapOfParams: Map<String, String>)
}