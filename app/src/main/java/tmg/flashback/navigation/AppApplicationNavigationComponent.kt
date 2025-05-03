package tmg.flashback.navigation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import tmg.aboutthisapp.AboutThisAppActivity
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.googleanalytics.manager.FirebaseAnalyticsManager
import tmg.flashback.device.ActivityProvider
import tmg.flashback.maintenance.presentation.forceupgrade.ForceUpgradeActivity
import tmg.flashback.notifications.navigation.NotificationNavigationProvider
import tmg.flashback.presentation.HomeActivity
import tmg.flashback.presentation.aboutthisapp.AboutThisAppConfigProvider
import tmg.flashback.presentation.sync.SyncActivity
import javax.inject.Inject

class AppApplicationNavigationComponent @Inject constructor(
    private val buildConfigManager: BuildConfigManager,
    private val firebaseAnalyticsManager: FirebaseAnalyticsManager,
    private val aboutThisAppConfigProvider: AboutThisAppConfigProvider,
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



    override fun aboutApp() {
        firebaseAnalyticsManager.viewScreen("About This App", mapOf(
            "version" to buildConfigManager.versionName
        ), AboutThisAppActivity::class.java)

        activityProvider.launch {
            val config = aboutThisAppConfigProvider.getConfig()
            it.startActivity(AboutThisAppActivity.intent(it, config))
        }
    }


    override fun syncActivity() = activityProvider.launch {
        it.startActivity(syncActivityIntent(it))
    }

    override fun syncActivityIntent(context: Context): Intent {
        return Intent(context, SyncActivity::class.java)
    }

    override fun settings() {
        navigator.navigate(Screen.Settings)
    }

    override fun forceUpgrade() = activityProvider.launch {
        it.startActivity(forceUpgradeIntent(it))
    }

    override fun forceUpgradeIntent(context: Context): Intent {
        return ForceUpgradeActivity.intent(context)
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