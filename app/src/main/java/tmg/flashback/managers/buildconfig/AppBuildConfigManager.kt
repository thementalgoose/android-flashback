package tmg.flashback.managers.buildconfig

import android.os.Build
import tmg.flashback.BuildConfig
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.navigation.ActivityProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppBuildConfigManager @Inject constructor(
    private val topActivityProvider: ActivityProvider
) : BuildConfigManager {

    override val versionCode: Int
        get() = BuildConfig.VERSION_CODE

    override val versionName: String
        get() = BuildConfig.VERSION_NAME

    override val applicationId: String
        get() = BuildConfig.APPLICATION_ID

    override val isNotificationChannelsSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    override val isMonetThemeSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    override val isAppShortcutsSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1

    override val isRuntimeNotificationsSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
}