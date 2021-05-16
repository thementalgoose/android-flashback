package tmg.flashback.managers

import android.content.Context
import android.content.Intent
import tmg.aboutthisapp.AboutThisAppActivity
import tmg.core.analytics.manager.AnalyticsManager
import tmg.core.device.controllers.DeviceController
import tmg.core.device.managers.BuildConfigManager
import tmg.core.ui.navigation.NavigationProvider
import tmg.flashback.constants.AboutThisAppConfig
import tmg.flashback.ui.SplashActivity
import tmg.notifications.navigation.NotificationNavigationProvider

class AppNavigationProvider(
    private val buildConfigManager: BuildConfigManager,
    private val deviceController: DeviceController,
    private val analyticsManager: AnalyticsManager
): NavigationProvider, NotificationNavigationProvider {

    override fun relaunchAppIntent(context: Context): Intent {
        analyticsManager.logEvent("Relaunch app")
        return Intent(context, SplashActivity::class.java)
    }

    override fun aboutAppIntent(context: Context): Intent {
        analyticsManager.viewScreen("About This App", AboutThisAppActivity::class.java, mapOf(
            "version" to buildConfigManager.versionName
        ))
        return AboutThisAppActivity.intent(context, AboutThisAppConfig.configuration(context,
            appVersion = buildConfigManager.versionName,
            deviceUdid = deviceController.deviceUdid
        ))
    }
}