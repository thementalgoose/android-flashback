package tmg.flashback.managers

import android.content.Context
import android.content.Intent
import tmg.aboutthisapp.AboutThisAppActivity
import tmg.core.device.controllers.DeviceController
import tmg.core.device.managers.BuildConfigManager
import tmg.core.ui.navigation.NavigationProvider
import tmg.flashback.constants.AboutThisAppConfig
import tmg.flashback.ui.SplashActivity
import tmg.notifications.navigation.NotificationNavigationProvider

class AppNavigationProvider(
    private val buildConfigManager: BuildConfigManager,
    private val deviceController: DeviceController
): NavigationProvider, NotificationNavigationProvider {

    override fun relaunchAppIntent(context: Context): Intent {
        return Intent(context, SplashActivity::class.java)
    }

    override fun aboutAppIntent(context: Context): Intent {
        return AboutThisAppActivity.intent(context, AboutThisAppConfig.configuration(context,
            appVersion = buildConfigManager.versionName,
            deviceUdid = deviceController.deviceUdid
        ))
    }
}