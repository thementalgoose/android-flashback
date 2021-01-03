package tmg.flashback.managers.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import tmg.flashback.managers.analytics.UserPropertiesManager

class FirebaseUserPropertiesManager(
    val context: Context
): UserPropertiesManager {

    private val keyOsVersion = "os_version"
    private val keyDeviceModel = "device_model"

    override fun setOsVersion(osVersion: String) {
        FirebaseAnalytics.getInstance(context).setUserProperty(keyOsVersion, osVersion)
    }

    override fun setDeviceModel(model: String) {
        FirebaseAnalytics.getInstance(context).setUserProperty(keyDeviceModel, model)
    }
}