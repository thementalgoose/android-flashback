package tmg.flashback

import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.github.stkent.bugshaker.BugShaker
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.device.controllers.DeviceController
import tmg.flashback.analytics.UserProperty.*
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.managers.widgets.WidgetManager
import tmg.flashback.ui.controllers.ThemeController
import tmg.flashback.ui.model.NightMode
import tmg.flashback.upnext.extensions.updateAllWidgets
import tmg.flashback.upnext.model.NotificationChannel
import tmg.flashback.notifications.controllers.NotificationController
import tmg.utilities.extensions.isInDayMode

/**
 * Startup handler
 *
 * Ran when the application is first started
 */
class FlashbackStartup(
    private val deviceController: DeviceController,
    private val crashController: CrashController,
    private val widgetManager: WidgetManager,
    private val themeController: ThemeController,
    private val analyticsManager: AnalyticsManager,
    private val notificationController: NotificationController
) {
    fun startup(application: FlashbackApplication) {

        // ThreeTen
        AndroidThreeTen.init(application)

        // Theming
        when (themeController.nightMode) {
            NightMode.DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            NightMode.DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            NightMode.NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        // Shake to report a bug
        if (crashController.shakeToReport) {
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
        crashController.initialise(
            deviceUdid = deviceController.deviceUdid,
            appOpenedCount = deviceController.appOpenedCount,
            appFirstOpened = deviceController.appFirstBoot
        )

        // Channels
        GlobalScope.launch {

            // Legacy: Remove these existing channels which were previously used for remote notifications
            notificationController.deleteNotificationChannel("race")
            notificationController.deleteNotificationChannel("qualifying")

            NotificationChannel.values().forEach {
                notificationController.createNotificationChannel(it.channelId, it.label)
            }
            notificationController.subscribeToRemoteNotifications()
        }

        // Initialise user properties
        analyticsManager.initialise(userId = deviceController.deviceUdid)
        analyticsManager.setUserProperty(DEVICE_MODEL, Build.MODEL)
        analyticsManager.setUserProperty(OS_VERSION, Build.VERSION.SDK_INT.toString())
        analyticsManager.setUserProperty(APP_VERSION, BuildConfig.VERSION_NAME)
        analyticsManager.setUserProperty(WIDGET_USAGE, if (widgetManager.hasWidgets) "true" else "false")
        analyticsManager.setUserProperty(DEVICE_THEME, when (themeController.nightMode) {
            NightMode.DAY -> "day"
            NightMode.NIGHT -> "night"
            NightMode.DEFAULT -> if (application.isInDayMode()) "day (default)" else "night (default)"
        })

        // Update Widgets
        application.updateAllWidgets()
    }
}