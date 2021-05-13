package tmg.flashback.managers.buildconfig

import tmg.core.device.managers.BuildConfigManager
import tmg.flashback.BuildConfig

class AppBuildConfigManager : BuildConfigManager {

    override val versionCode: Int
        get() = BuildConfig.VERSION_CODE

    override val versionName: String
        get() = BuildConfig.VERSION_NAME

    override val applicationId: String
        get() = BuildConfig.APPLICATION_ID
}