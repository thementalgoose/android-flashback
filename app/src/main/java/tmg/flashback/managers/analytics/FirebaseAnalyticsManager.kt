package tmg.flashback.managers.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import tmg.flashback.core.managers.AnalyticsManager

class FirebaseAnalyticsManager(
    val context: Context
): AnalyticsManager {

    override fun logEvent(key: String, bundle: Bundle) {
        FirebaseAnalytics.getInstance(context).logEvent(key, bundle)
    }

    override fun logViewScreen(
        screenName: String,
        clazz: Class<*>,
        mapOfParams: Map<String, String>
    ) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, clazz.simpleName)
            for (x in mapOfParams) {
                putString(x.key, x.value)
            }
        }
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    override fun setProperty(key: String, value: String) {
        FirebaseAnalytics.getInstance(context).setUserProperty(key, value)
    }
}