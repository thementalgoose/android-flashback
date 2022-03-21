package tmg.flashback.managers

import android.content.Context
import android.content.Intent
import tmg.aboutthisapp.AboutThisAppActivity
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.constants.AboutThisAppConfig
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.notifications.navigation.NotificationNavigationProvider
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.ui.HomeActivity
import tmg.flashback.ui.navigation.NavigationProvider
import tmg.flashback.ui.sync.SyncActivity

class AppNavigationProvider(
    private val buildConfigManager: BuildConfigManager,
    private val deviceRepository: DeviceRepository,
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
            deviceUdid = deviceRepository.deviceUdid,
            rssSources = if (rssController.enabled) rssController.sources else emptyList()
        ))
    }

    override fun syncActivityIntent(context: Context): Intent {
        return Intent(context, SyncActivity::class.java)
    }
}