package tmg.flashback.managers

import android.content.Context
import android.content.Intent
import tmg.aboutthisapp.AboutThisAppActivity
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.device.controllers.DeviceController
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.ui.navigation.NavigationProvider
import tmg.flashback.constants.AboutThisAppConfig
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.ui.dashboard.HomeActivity
import tmg.notifications.navigation.NotificationNavigationProvider

class AppNavigationProvider(
    private val buildConfigManager: BuildConfigManager,
    private val deviceController: DeviceController,
    private val analyticsManager: AnalyticsManager,
    private val rssController: RSSController
): NavigationProvider, NotificationNavigationProvider {

    override fun relaunchAppIntent(context: Context): Intent {
        analyticsManager.logEvent("relaunch_app")
        return Intent(context, HomeActivity::class.java)
    }

    override fun aboutAppIntent(context: Context): Intent {
        analyticsManager.viewScreen("open_about_this_app", AboutThisAppActivity::class.java, mapOf(
            "version" to buildConfigManager.versionName
        ))
        return AboutThisAppActivity.intent(context, AboutThisAppConfig.configuration(context,
            appVersion = buildConfigManager.versionName,
            deviceUdid = deviceController.deviceUdid,
            rssSources = if (rssController.enabled) rssController.sources else emptyList()
        ))
    }
}