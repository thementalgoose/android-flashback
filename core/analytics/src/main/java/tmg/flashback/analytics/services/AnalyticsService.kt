package tmg.flashback.analytics.services

import android.os.Bundle

/**
 * Wrapper around the Firebase Analytics package that we use
 * Abstracted for testing
 */
interface AnalyticsService {

    fun setProperty(key: String, value: String)

    fun logEvent(key: String, bundle: Bundle? = null)

    fun logViewScreen(screenName: String, mapOfParams: Map<String, String>, clazz: Class<*>? = null)

    fun setUserId(userId: String)

    fun setAnalyticsCollectionEnabled(enabled: Boolean)
}