package tmg.flashback

import android.os.Build
import android.util.Log
import com.github.stkent.bugshaker.BugShaker
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import tmg.flashback.controllers.CrashController
import tmg.flashback.controllers.DeviceController
import tmg.flashback.controllers.NotificationController
import tmg.flashback.extensions.updateAllWidgets
import tmg.flashback.managers.analytics.UserPropertiesManager
import tmg.flashback.managers.remoteconfig.RemoteConfigManager
import tmg.flashback.notifications.PushNotificationManager
import tmg.flashback.repo.config.RemoteConfigRepository

/**
 * Startup handler
 *
 * Ran when the application is first started
 */
class FlashbackStartup(
        private val deviceController: DeviceController,
        private val prefsNotification: NotificationController,
        private val configRepository: RemoteConfigManager,
        private val crashController: CrashController,
        private val analyticsUserProperties: UserPropertiesManager,
        private val notificationManager: PushNotificationManager,
) {
    fun startup(application: FlashbackApplication) {

        // ThreeTen
        AndroidThreeTen.init(application)

        // Shake to report a bug
        if (deviceController.shakeToReport) {
            Log.i("Flashback", "Enabling shake to report")
            BugShaker.get(application)
                    .setEmailAddresses("thementalgoose@gmail.com")
                    .setEmailSubjectLine("${application.getString(R.string.app_name)} - App Feedback")
                    .setAlertDialogType(AlertDialogType.APP_COMPAT)
                    .setLoggingEnabled(BuildConfig.DEBUG)
                    .assemble()
                    .start()
        }

        // App startup
        deviceController.appFirstBoot
        deviceController.appOpened()

        // Crash Reporting
        crashController.initialiseCrashReporting()

        // Channels
        notificationManager.createChannels()

        // Opt in to all notifications
        notificationsOptIn()

        // Initialise user properties
        analyticsUserProperties.setDeviceModel(Build.MODEL)
        analyticsUserProperties.setOsVersion(Build.VERSION.SDK_INT.toString())

        // Update Widgets
        application.updateAllWidgets()
    }

    fun notificationsOptIn() {

        // Enrol for race push notifications
        if (prefsNotification.raceOptInUndecided) {
            GlobalScope.launch {
                val result = notificationManager.raceSubscribe()
                Log.i("Flashback", "Auto enrol push notifications race - $result")
            }
        }

        // Enrol for qualifying push notifications
        if (prefsNotification.qualifyingOptInUndecided) {
            GlobalScope.launch {
                val result = notificationManager.qualifyingSubscribe()
                Log.i("Flashback", "Auto enrol push notifications qualifying - $result")
            }
        }

        // Enrol for qualifying push notifications
        if (prefsNotification.miscOptInUndecided) {
            GlobalScope.launch {
                val result = notificationManager.appSupportSubscribe()
                Log.i("Flashback", "Auto enrol push notifications misc - $result")
            }
        }
    }
}