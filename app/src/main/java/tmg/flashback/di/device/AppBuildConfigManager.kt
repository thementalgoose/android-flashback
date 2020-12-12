package tmg.flashback.di.device

import tmg.flashback.BuildConfig

class AppBuildConfigManager: BuildConfigManager {

    override val versionCode: Int
        get() = BuildConfig.VERSION_CODE

    override val versionName: String
        get() = BuildConfig.VERSION_NAME

    override val autoEnrolPushNotifications: Boolean
        get() = BuildConfig.AUTO_ENROL_PUSH_NOTIFICATIONS
}