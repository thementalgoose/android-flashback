package tmg.core.analytics.services

import android.os.Bundle

/**
 * Wrapper around the Firebase Analytics package that we use
 * Abstracted for testing
 */
interface AnalyticsService {

    fun setProperty(key: String, value: String)

    fun logEvent(key: String, bundle: Bundle? = null)

    fun logViewScreen(screenName: String, clazz: Class<*>, mapOfParams: Map<String, String>)
}