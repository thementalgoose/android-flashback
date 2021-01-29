package tmg.flashback.managers.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import tmg.flashback.BuildConfig
import tmg.flashback.managers.analytics.UserPropertiesManager
import tmg.flashback.repo.pref.DeviceRepository
import tmg.flashback.rss.managers.RSSAnalyticsManager

class FirebaseAnalyticsManager(
    val context: Context,
    val deviceRepository: DeviceRepository
): UserPropertiesManager, AnalyticsManager, RSSAnalyticsManager {

    override var enableAnalytics: Boolean
        get() = deviceRepository.optInAnalytics
        set(value) {
            deviceRepository.optInAnalytics = value
        }

    override fun setProperty(key: UserProperty, value: String) {
        FirebaseAnalytics.getInstance(context).setUserProperty(key.key, value)
    }

    override fun viewScreen(screenName: String, clazz: Class<*>, mapOfParams: Map<String, String>) {
        if (BuildConfig.DEBUG) {
            Log.i("Flashback", "Analytics Screen viewed $screenName - $mapOfParams")
        }
        if (enableAnalytics) {
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, clazz.simpleName)
                for (x in mapOfParams) {
                    putString(x.key, x.value)
                }
            }
            FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
        }
    }

    override fun rssViewScreen(screenName: String, clazz: Class<*>, mapOfParams: Map<String, String>) {
        viewScreen(screenName, clazz, mapOfParams)
    }
}