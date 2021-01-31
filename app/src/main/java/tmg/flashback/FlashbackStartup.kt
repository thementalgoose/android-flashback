package tmg.flashback

import android.os.Build
import android.util.Log
import com.github.stkent.bugshaker.BugShaker
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tmg.flashback.controllers.NotificationController
import tmg.flashback.core.controllers.AnalyticsController
import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.core.controllers.CrashController
import tmg.flashback.core.controllers.DeviceController
import tmg.flashback.core.enums.UserProperty.*
import tmg.flashback.extensions.updateAllWidgets
import tmg.flashback.managers.notifications.PushNotificationManager

/**
 * Startup handler
 *
 * Ran when the application is first started
 */
class FlashbackStartup(
    private val deviceController: DeviceController,
    private val prefsNotification: NotificationController,
    private val crashController: CrashController,
    private val analyticsController: AnalyticsController,
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
        crashController.initialise()

        // Channels
        notificationManager.createChannels()

        // Opt in to all notifications
        notificationsOptIn()

        // Initialise user properties
        analyticsController.setUserProperty(DEVICE_MODEL, Build.MODEL)
        analyticsController.setUserProperty(OS_VERSION, Build.VERSION.SDK_INT.toString())
        analyticsController.setUserProperty(APP_VERSION, BuildConfig.VERSION_NAME)

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