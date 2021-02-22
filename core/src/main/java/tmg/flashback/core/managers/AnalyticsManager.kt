package tmg.flashback.core.managers

import android.os.Bundle

/**
 * Wrapper around the Firebase Analytics package that we use
 * Abstracted for testing
 */
interface AnalyticsManager {

    fun setProperty(key: String, value: String)

    fun logEvent(key: String, bundle: Bundle)

    fun logViewScreen(screenName: String, clazz: Class<*>, mapOfParams: Map<String, String>)
}