package tmg.flashback.managers

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import tmg.aboutthisapp.AboutThisAppActivity
import tmg.flashback.constants.AboutThisAppConfig
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.googleanalytics.manager.FirebaseAnalyticsManager
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
    private val firebaseAnalyticsManager: FirebaseAnalyticsManager,
    private val allSupportedSourcesUseCase: AllSupportedSourcesUseCase,
    private val activityProvider: ActivityProvider,
    private val navigator: Navigator,
): ApplicationNavigationComponent, NotificationNavigationProvider {

    override fun relaunchApp() = activityProvider.launch {
        it.startActivity(relaunchAppIntent(it))
    }

    override fun relaunchAppIntent(context: Context): Intent {
        firebaseAnalyticsManager.logEvent("relaunch_app")
        return Intent(context, HomeActivity::class.java)
    }



    override fun aboutApp() = activityProvider.launch {
        it.startActivity(aboutAppIntent(it))
    }

    override fun aboutAppIntent(context: Context): Intent {
        firebaseAnalyticsManager.viewScreen("About This App", mapOf(
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


    override fun appSettingsNotifications() = activityProvider.launch {
        it.startActivity(appSettingsNotificationsIntent())
    }
    override fun appSettingsNotificationsIntent(): Intent {
        val settingsIntent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        settingsIntent.putExtra("app_package", buildConfigManager.applicationId);
        settingsIntent.putExtra(Settings.EXTRA_APP_PACKAGE, buildConfigManager.applicationId)
        return settingsIntent
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun appSettingsSpecialPermissions() = activityProvider.launch {
        it.startActivity(appSettingsSpecialPermissionsIntent())
    }
    @RequiresApi(Build.VERSION_CODES.S)
    override fun appSettingsSpecialPermissionsIntent(): Intent {
        return Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
    }
}