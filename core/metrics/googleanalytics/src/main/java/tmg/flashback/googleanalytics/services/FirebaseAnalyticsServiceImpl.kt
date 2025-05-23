package tmg.flashback.googleanalytics.services

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.googleanalytics.BuildConfig
import javax.inject.Inject

internal class FirebaseAnalyticsServiceImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
): FirebaseAnalyticsService {

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
        mapOfParams: Map<String, String>,
        clazz: Class<*>?
    ) {
        if (BuildConfig.DEBUG) {
            Log.i("Analytics", "View screen $screenName -> $mapOfParams")
        }
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            if (clazz != null) {
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, clazz.simpleName)
            }
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