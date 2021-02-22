package tmg.flashback.managers.buildconfig

import tmg.flashback.BuildConfig
import tmg.flashback.core.managers.BuildConfigManager

class AppBuildConfigManager : BuildConfigManager {

    override val versionCode: Int
        get() = BuildConfig.VERSION_CODE

    override val versionName: String
        get() = BuildConfig.VERSION_NAME
}