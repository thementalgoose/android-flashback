package tmg.flashback

import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.jakewharton.threetenabp.AndroidThreeTen
import com.linkedin.android.shaky.EmailShakeDelegate
import com.linkedin.android.shaky.Shaky
import kotlinx.coroutines.*
import tmg.flashback.ads.usecases.InitialiseAdsUseCase
import tmg.flashback.analytics.UserProperty.*
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.configuration.usecases.InitialiseConfigUseCase
import tmg.flashback.crash_reporting.repository.CrashRepository
import tmg.flashback.crash_reporting.usecases.InitialiseCrashReportingUseCase
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.device.usecases.AppOpenedUseCase
import tmg.flashback.managers.widgets.WidgetManager
import tmg.flashback.notifications.managers.SystemNotificationManager
import tmg.flashback.notifications.usecases.RemoteNotificationSubscribeUseCase
import tmg.flashback.notifications.usecases.RemoteNotificationUnsubscribeUseCase
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.extensions.updateAllWidgets
import tmg.flashback.statistics.repository.models.NotificationChannel
import tmg.flashback.statistics.workmanager.WorkerProvider
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.repository.ThemeRepository
import tmg.utilities.extensions.isInDayMode

/**
 * Startup handler
 *
 * Ran when the application is first started
 */
class FlashbackStartup(
    private val deviceRepository: DeviceRepository,
    private val crashRepository: CrashRepository,
    private val initialiseCrashReportingUseCase: InitialiseCrashReportingUseCase,
    private val widgetManager: WidgetManager,
    private val scheduleController: ScheduleController,
    private val themeRepository: ThemeRepository,
    private val analyticsManager: AnalyticsManager,
    private val initialiseConfigUseCase: InitialiseConfigUseCase,
    private val systemNotificationManager: SystemNotificationManager,
    private val remoteNotificationSubscribeUseCase: RemoteNotificationSubscribeUseCase,
    private val remoteNotificationUnsubscribeUseCase: RemoteNotificationUnsubscribeUseCase,
    private val workerProvider: WorkerProvider,
    private val appOpenedUseCase: AppOpenedUseCase,
    private val initialiseAdsUseCase: InitialiseAdsUseCase
) {
    fun startup(application: FlashbackApplication) {

        // ThreeTen
        AndroidThreeTen.init(application)

        // Theming
        when (themeRepository.nightMode) {
            NightMode.DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            NightMode.DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            NightMode.NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        // Remote config
        initialiseConfigUseCase.initialise()

        // Shake to report a bug
        if (crashRepository.shakeToReport) {
            Log.i("Startup", "Enabling shake to report")

            Shaky.with(application, object : EmailShakeDelegate("thementalgoose@gmail.com") {
                override fun getTheme() = super.getTheme()
                override fun getPopupTheme() = super.getPopupTheme()
            })
        }

        // App startup
        appOpenedUseCase.run()

        // Crash Reporting
        initialiseCrashReportingUseCase.initialise(
            deviceUdid = deviceRepository.deviceUdid,
            appOpenedCount = deviceRepository.appOpenedCount,
            appFirstOpened = deviceRepository.appFirstOpened
        )

        // Adverts
        initialiseAdsUseCase.initialise()

        //region Notifications Legacy: Remove these existing channels which were previously used for remote notifications
        systemNotificationManager.cancelChannel("race")
        systemNotificationManager.cancelChannel("qualifying")
        //endregion

        // Notifications
        NotificationChannel.values().forEach {
            systemNotificationManager.createChannel(it.channelId, it.label)
        }

        systemNotificationManager.createChannel(
            "notify_race",
            R.string.notification_channel_race_notify
        )
        systemNotificationManager.createChannel(
            "notify_qualifying",
            R.string.notification_channel_qualifying_notify
        )
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        applicationScope.launch(Dispatchers.IO) {
            when (scheduleController.notificationQualifyingNotify) {
                true -> remoteNotificationSubscribeUseCase.subscribe("notify_qualifying")
                false -> remoteNotificationUnsubscribeUseCase.unsubscribe("notify_qualifying")
            }
            when (scheduleController.notificationRaceNotify) {
                true -> remoteNotificationSubscribeUseCase.subscribe("notify_race")
                false -> remoteNotificationUnsubscribeUseCase.unsubscribe("notify_race")
            }
        }

        // Initialise user properties
        analyticsManager.initialise(userId = deviceRepository.deviceUdid)
        analyticsManager.setUserProperty(DEVICE_MODEL, Build.MODEL)
        analyticsManager.setUserProperty(OS_VERSION, Build.VERSION.SDK_INT.toString())
        analyticsManager.setUserProperty(APP_VERSION, BuildConfig.VERSION_NAME)
        analyticsManager.setUserProperty(
            WIDGET_USAGE,
            if (widgetManager.hasWidgets) "true" else "false"
        )
        analyticsManager.setUserProperty(
            DEVICE_THEME, when (themeRepository.nightMode) {
                NightMode.DAY -> "day"
                NightMode.NIGHT -> "night"
                NightMode.DEFAULT -> if (application.isInDayMode()) "day (default)" else "night (default)"
            }
        )

        // Update Widgets
        application.updateAllWidgets()
    }
}
