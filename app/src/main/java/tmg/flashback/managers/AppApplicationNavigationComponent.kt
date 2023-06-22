package tmg.flashback.managers

import android.content.Context
import android.content.Intent
import tmg.aboutthisapp.AboutThisAppActivity
import tmg.flashback.googleanalytics.manager.AnalyticsManager
import tmg.flashback.constants.AboutThisAppConfig
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.navigation.ActivityProvider
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.navigation.Navigator
import tmg.flashback.notifications.navigation.NotificationNavigationProvider
import tmg.flashback.repositories.ContactRepository
import tmg.flashback.rss.usecases.AllSupportedSourcesUseCase
import tmg.flashback.ui.HomeActivity
import tmg.flashback.ui.settings.All
import tmg.flashback.ui.sync.SyncActivity
import javax.inject.Inject

class AppApplicationNavigationComponent @Inject constructor(
    private val buildConfigManager: BuildConfigManager,
    private val deviceRepository: DeviceRepository,
    private val contactRepository: ContactRepository,
    private val analyticsManager: AnalyticsManager,
    private val allSupportedSourcesUseCase: AllSupportedSourcesUseCase,
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
            contactEmail = contactRepository.contactEmail,
            deviceUdid = "${deviceRepository.deviceUdid}\n${deviceRepository.installationId}",
            rssSources = allSupportedSourcesUseCase.getSources()
        ))
    }



    override fun syncActivity() = activityProvider.launch {
        it.startActivity(syncActivityIntent(it))
    }

    override fun syncActivityIntent(context: Context): Intent {
        return Intent(context, SyncActivity::class.java)
    }

    override fun settings() {
        navigator.navigate(tmg.flashback.navigation.Screen.Settings.All)
    }
}