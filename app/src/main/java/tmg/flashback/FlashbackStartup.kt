package tmg.flashback

import android.os.Build
import android.util.Log
import com.github.stkent.bugshaker.BugShaker
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.device.controllers.DeviceController
import tmg.flashback.core.enums.Theme
import tmg.analytics.UserProperty.*
import tmg.analytics.controllers.AnalyticsController
import tmg.flashback.core.repositories.CoreRepository
import tmg.notifications.managers.PushNotificationManager
import tmg.flashback.managers.widgets.WidgetManager
import tmg.flashback.statistics.extensions.updateAllWidgets
import tmg.notifications.controllers.NotificationController
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
    private val coreRepository: CoreRepository,
    private val analyticsController: AnalyticsController,
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
        analyticsController.setUserProperty(DEVICE_MODEL, Build.MODEL)
        analyticsController.setUserProperty(OS_VERSION, Build.VERSION.SDK_INT.toString())
        analyticsController.setUserProperty(APP_VERSION, BuildConfig.VERSION_NAME)
        analyticsController.setUserProperty(WIDGET_USAGE, if (widgetManager.hasWidgets) "true" else "false")
        analyticsController.setUserProperty(DEVICE_THEME, when (coreRepository.theme) {
            Theme.DAY -> "day"
            Theme.NIGHT -> "night"
            Theme.AUTO -> if (application.isInDayMode()) "day" else "night"
        })

        // Update Widgets
        application.updateAllWidgets()
    }
}