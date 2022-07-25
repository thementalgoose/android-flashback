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
import tmg.flashback.ui.All
import tmg.flashback.ui.Home
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui.HomeActivity
import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.ui.sync.SyncActivity

class AppApplicationNavigationComponent(
    private val buildConfigManager: BuildConfigManager,
    private val deviceRepository: DeviceRepository,
    private val analyticsManager: AnalyticsManager,
    private val rssController: RSSController,
    private val activityProvider: ActivityProvider,
    private val navigator: Navigator,
): ApplicationNavigationComponent, NotificationNavigationProvider {

    override fun relaunchApp() = activityProvider.launch {
        it.startActivity(relaunchAppIntent(it))
    }

    override fun relaunchAppIntent(context: Context): Intent {
        analyticsManager.logEvent("relaunch_app")
        return Intent(context, HomeActivity::class.java)
    }



    override fun aboutApp() = activityProvider.launch {
        it.startActivity(aboutAppIntent(it))
    }

    override fun aboutAppIntent(context: Context): Intent {
        analyticsManager.viewScreen("About This App", mapOf(
            "version" to buildConfigManager.versionName
        ), AboutThisAppActivity::class.java)
        return AboutThisAppActivity.intent(context, AboutThisAppConfig.configuration(context,
            appVersion = buildConfigManager.versionName,
            deviceUdid = deviceRepository.deviceUdid,
            rssSources = if (rssController.enabled) rssController.sources else emptyList()
        ))
    }



    override fun syncActivity() = activityProvider.launch {
        it.startActivity(syncActivityIntent(it))
    }

    override fun syncActivityIntent(context: Context): Intent {
        return Intent(context, SyncActivity::class.java)
    }



    override fun settings() {
        navigator.navigate(Screen.Settings.All)
    }

    override fun home() {
        navigator.navigate(Screen.Home)
    }
}