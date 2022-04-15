package tmg.flashback.managers

import android.content.Context
import android.content.Intent
import android.net.Uri
import tmg.aboutthisapp.AboutThisAppActivity
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.constants.AboutThisAppConfig
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.notifications.navigation.NotificationNavigationProvider
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui.HomeActivity
import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.ui.sync.SyncActivity
import tmg.flashback.ui2.settings.SettingsAllActivity
import java.net.MalformedURLException

class AppApplicationNavigationComponent(
    private val buildConfigManager: BuildConfigManager,
    private val deviceRepository: DeviceRepository,
    private val analyticsManager: AnalyticsManager,
    private val rssController: RSSController,
    private val activityProvider: ActivityProvider
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
        analyticsManager.viewScreen("open_about_this_app", AboutThisAppActivity::class.java, mapOf(
            "version" to buildConfigManager.versionName
        ))
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



    override fun settings() = activityProvider.launch {
        it.startActivity(settingsIntent(it))
    }

    override fun settingsIntent(context: Context): Intent {
        return SettingsAllActivity.intent(context)
    }

    override fun openUrl(url: String) {
        val uri = try { Uri.parse(url) } catch (e: MalformedURLException) { null } ?: return
        val activity = activityProvider.activity ?: return
        val intent = Intent(Intent.ACTION_VIEW, uri)
        activity.startActivity(intent)
    }
}