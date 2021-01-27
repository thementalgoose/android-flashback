package tmg.flashback.managers.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import tmg.flashback.managers.analytics.UserPropertiesManager

class FirebaseAnalyticsManager(
    val context: Context
): UserPropertiesManager, AnalyticsManager {

    private val keyOsVersion = "os_version"
    private val keyDeviceModel = "device_model"

    override fun setOsVersion(osVersion: String) {
        FirebaseAnalytics.getInstance(context).setUserProperty(keyOsVersion, osVersion)
    }

    override fun setDeviceModel(model: String) {
        FirebaseAnalytics.getInstance(context).setUserProperty(keyDeviceModel, model)
    }

    override fun viewScreen(screenName: String, clazz: Class<*>, mapOfParams: Map<String, String>) {
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