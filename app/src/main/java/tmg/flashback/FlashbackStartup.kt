package tmg.flashback

import android.os.Build
import android.util.Log
import com.github.stkent.bugshaker.BugShaker
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tmg.crash_reporting.controllers.CrashController
import tmg.core.device.controllers.DeviceController
import tmg.core.analytics.UserProperty.*
import tmg.core.analytics.manager.AnalyticsManager
import tmg.flashback.managers.widgets.WidgetManager
import tmg.core.ui.controllers.ThemeController
import tmg.core.ui.model.Theme
import tmg.flashback.statistics.extensions.updateAllWidgets
import tmg.notifications.controllers.NotificationController
import tmg.utilities.extensions.isInDayMode

/**
 * Startup handler
 *
 * Ran when the application is first started
 */
class FlashbackStartup(
    private val deviceController: tmg.core.device.controllers.DeviceController,
    private val crashController: CrashController,
    private val widgetManager: WidgetManager,
    private val themeController: ThemeController,
    private val analyticsManager: AnalyticsManager,
    private val notificationController: NotificationController
) {
    fun startup(application: FlashbackApplication) {

        // ThreeTen
        AndroidThreeTen.init(application)

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
        crashController.initialise()

        // Channels
        GlobalScope.launch {
            notificationController.createNotificationChannels()
            notificationController.subscribe()
        }

        // Initialise user properties
        analyticsManager.setUserProperty(DEVICE_MODEL, Build.MODEL)
        analyticsManager.setUserProperty(OS_VERSION, Build.VERSION.SDK_INT.toString())
        analyticsManager.setUserProperty(APP_VERSION, BuildConfig.VERSION_NAME)
        analyticsManager.setUserProperty(WIDGET_USAGE, if (widgetManager.hasWidgets) "true" else "false")
        analyticsManager.setUserProperty(DEVICE_THEME, when (themeController.theme) {
            Theme.DAY -> "day"
            Theme.NIGHT -> "night"
            Theme.DEFAULT -> if (application.isInDayMode()) "day (default)" else "night (default)"
        })

        // Update Widgets
        application.updateAllWidgets()
    }
}