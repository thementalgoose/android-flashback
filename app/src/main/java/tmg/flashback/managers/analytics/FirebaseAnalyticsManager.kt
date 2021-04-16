package tmg.flashback.managers.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import tmg.flashback.core.BuildConfig
import tmg.flashback.core.managers.AnalyticsManager

class FirebaseAnalyticsManager(
    val context: Context
): AnalyticsManager {

    private val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun logEvent(key: String, bundle: Bundle?) {
        if (BuildConfig.DEBUG) {
            Log.i("Flashback Analytics", "Log Event $key | ${bundle?.keySet()?.joinToString(separator = ",")}")
        }
        analytics.logEvent(key, bundle)
    }

    override fun logViewScreen(
        screenName: String,
        clazz: Class<*>,
        mapOfParams: Map<String, String>
    ) {
        if (BuildConfig.DEBUG) {
            Log.i("Flashback Analytics", "View screen $screenName ($clazz) -> $mapOfParams")
        }
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, clazz.simpleName)
            for (x in mapOfParams) {
                putString(x.key, x.value)
            }
        }
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    override fun setProperty(key: String, value: String) {
        if (BuildConfig.DEBUG) {
            Log.i("Flashback Analytics", "UserProperty $key -> $value")
        }
        analytics.setUserProperty(key, value)
    }
}