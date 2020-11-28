package tmg.flashback.di.device

import android.os.Build
import tmg.flashback.BuildConfig

class AppBuildConfigProvider: BuildConfigProvider {

    override val versionCode: Int
        get() = BuildConfig.VERSION_CODE

    override val versionName: String
        get() = BuildConfig.VERSION_NAME

    override val autoEnrolPushNotifications: Boolean
        get() = BuildConfig.AUTO_ENROL_PUSH_NOTIFICATIONS
}