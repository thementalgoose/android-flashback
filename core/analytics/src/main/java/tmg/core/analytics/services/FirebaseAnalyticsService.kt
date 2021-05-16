package tmg.core.analytics.services

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import tmg.core.analytics.BuildConfig

internal class FirebaseAnalyticsService(
    val context: Context
): AnalyticsService {

    private val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun logEvent(key: String, bundle: Bundle?) {
        if (BuildConfig.DEBUG) {
            Log.i(
                "Analytics",
                "Log Event $key | ${bundle.toString()}"
            )
        }
        analytics.logEvent(key, bundle)
    }

    override fun logViewScreen(
        screenName: String,
        clazz: Class<*>,
        mapOfParams: Map<String, String>
    ) {
        if (BuildConfig.DEBUG) {
            Log.i("Analytics", "View screen $screenName -> $mapOfParams")
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

    override fun setUserId(userId: String) {
        if (BuildConfig.DEBUG) {
            Log.i("Analytics", "UserId $userId")
        }
        analytics.setUserId(userId)
    }

    override fun setAnalyticsCollectionEnabled(enabled: Boolean) {
        if (BuildConfig.DEBUG) {
            Log.i("Analytics", "Analytics collection $enabled")
        }
        analytics.setAnalyticsCollectionEnabled(enabled)
    }

    override fun setProperty(key: String, value: String) {
        if (BuildConfig.DEBUG) {
            Log.i("Analytics", "UserProperty $key -> $value")
        }
        analytics.setUserProperty(key, value)
    }
}