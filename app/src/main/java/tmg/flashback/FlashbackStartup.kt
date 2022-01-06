package tmg.flashback

import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.jakewharton.threetenabp.AndroidThreeTen
import com.linkedin.android.shaky.EmailShakeDelegate
import com.linkedin.android.shaky.ShakeDelegate
import com.linkedin.android.shaky.Shaky
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tmg.flashback.ads.controller.AdsController
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.device.controllers.DeviceController
import tmg.flashback.analytics.UserProperty.*
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.common.controllers.ForceUpgradeController
import tmg.flashback.configuration.controllers.ConfigController
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.managers.widgets.WidgetManager
import tmg.flashback.ui.controllers.ThemeController
import tmg.flashback.ui.model.NightMode
import tmg.flashback.statistics.extensions.updateAllWidgets
import tmg.flashback.statistics.repository.models.NotificationChannel
import tmg.flashback.notifications.controllers.NotificationController
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.workmanager.WorkerProvider
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
    private val scheduleController: ScheduleController,
    private val themeController: ThemeController,
    private val analyticsManager: AnalyticsManager,
    private val notificationController: NotificationController,
    private val workerProvider: WorkerProvider,
    private val adsController: AdsController
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
            Log.i("Startup", "Enabling shake to report")

            Shaky.with(application, object : EmailShakeDelegate("thementalgoose@gmail.com") {
                override fun getTheme() = super.getTheme()
                override fun getPopupTheme() = super.getPopupTheme()
            })
        }

        // App startup
        deviceController.appFirstBoot

        // Crash Reporting
        crashController.initialise(
            deviceUdid = deviceController.deviceUdid,
            appOpenedCount = deviceController.appOpenedCount,
            appFirstOpened = deviceController.appFirstBoot
        )

        // Adverts
        if (adsController.areAdvertsEnabled) {
            adsController.initialise(application)
        }


        //region Notifications Legacy: Remove these existing channels which were previously used for remote notifications
        notificationController.deleteNotificationChannel("race")
        notificationController.deleteNotificationChannel("qualifying")
        //endregion

        // Notifications
        NotificationChannel.values().forEach {
            notificationController.createNotificationChannel(it.channelId, it.label)
        }

        // TODO: Results available notifications - Remove
        if (BuildConfig.DEBUG) {
            notificationController.createNotificationChannel("notify_race", R.string.notification_channel_race_notify)
            notificationController.createNotificationChannel("notify_qualifying", R.string.notification_channel_qualifying_notify)
            GlobalScope.launch {
                when (scheduleController.notificationQualifyingNotify) {
                    true -> notificationController.subscribeToRemoteNotification("notify_qualifying")
                    false -> notificationController.unsubscribeToRemoteNotification("notify_qualifying")
                }
                when (scheduleController.notificationRaceNotify) {
                    true -> notificationController.subscribeToRemoteNotification("notify_race")
                    false -> notificationController.unsubscribeToRemoteNotification("notify_race")
                }
            }
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

        // Content sync
        workerProvider.contentSync()

        // Update Widgets
        application.updateAllWidgets()
    }
}
