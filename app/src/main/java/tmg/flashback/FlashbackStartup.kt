package tmg.flashback

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.jakewharton.threetenabp.AndroidThreeTen
import com.linkedin.android.shaky.EmailShakeDelegate
import com.linkedin.android.shaky.Result
import com.linkedin.android.shaky.Shaky
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import tmg.flashback.ads.ads.usecases.InitialiseAdsUseCase
import tmg.flashback.googleanalytics.UserProperty.APP_VERSION
import tmg.flashback.googleanalytics.UserProperty.DEVICE_MODEL
import tmg.flashback.googleanalytics.UserProperty.DEVICE_THEME
import tmg.flashback.googleanalytics.UserProperty.OS_VERSION
import tmg.flashback.googleanalytics.UserProperty.WIDGET_USAGE
import tmg.flashback.googleanalytics.manager.AnalyticsManager
import tmg.flashback.configuration.usecases.InitialiseConfigUseCase
import tmg.flashback.crashlytics.usecases.InitialiseCrashReportingUseCase
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.device.repository.PrivacyRepository
import tmg.flashback.device.usecases.AppOpenedUseCase
import tmg.flashback.device.usecases.GetDeviceInfoUseCase
import tmg.flashback.notifications.managers.SystemNotificationManager
import tmg.flashback.notifications.usecases.RemoteNotificationSubscribeUseCase
import tmg.flashback.notifications.usecases.RemoteNotificationUnsubscribeUseCase
import tmg.flashback.repositories.ContactRepository
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.repository.models.NotificationChannel
import tmg.flashback.style.AppTheme
import tmg.flashback.style.SupportedTheme
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.model.Theme
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.widgets.contract.usecases.HasWidgetsUseCase
import tmg.flashback.widgets.contract.usecases.UpdateWidgetsUseCase
import tmg.utilities.extensions.isInDayMode
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Startup handler
 *
 * Ran when the application is first started
 */
@Singleton
class FlashbackStartup @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val privacyRepository: PrivacyRepository,
    private val contactRepository: ContactRepository,
    private val initialiseCrashReportingUseCase: InitialiseCrashReportingUseCase,
    private val notificationRepository: NotificationsRepositoryImpl,
    private val themeRepository: ThemeRepository,
    private val analyticsManager: AnalyticsManager,
    private val initialiseConfigUseCase: InitialiseConfigUseCase,
    private val systemNotificationManager: SystemNotificationManager,
    private val remoteNotificationSubscribeUseCase: RemoteNotificationSubscribeUseCase,
    private val remoteNotificationUnsubscribeUseCase: RemoteNotificationUnsubscribeUseCase,
    private val appOpenedUseCase: AppOpenedUseCase,
    private val getDeviceInfoUseCase: GetDeviceInfoUseCase,
    private val initialiseAdsUseCase: InitialiseAdsUseCase,
    private val updateWidgetsUseCase: UpdateWidgetsUseCase,
    private val hasWidgetsUseCase: HasWidgetsUseCase,
) {
    fun startup(application: FlashbackApplication) {

        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        // ThreeTen
        AndroidThreeTen.init(application)

        // Theming
        AppTheme.appTheme = when (themeRepository.theme) {
            Theme.DEFAULT -> SupportedTheme.Default
            Theme.MATERIAL_YOU -> SupportedTheme.MaterialYou
        }
        when (themeRepository.nightMode) {
            NightMode.DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            NightMode.DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            NightMode.NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        // Remote config
        initialiseConfigUseCase.initialise()

        // Shake to report a bug
        if (privacyRepository.shakeToReport) {
            Log.i("Startup", "Enabling shake to report")
            Shaky.with(application, object : EmailShakeDelegate(contactRepository.contactEmail) {
                override fun onSubmit(result: Result): Intent {
                    val intent = super.onSubmit(result)
                    val text = intent.extras?.getString(Intent.EXTRA_TEXT)
                    intent.putExtra(Intent.EXTRA_TEXT, "${text}\n\n${getDeviceInfoUseCase.run()}")
                    return intent
                }
            })
        }

        // App startup
        appOpenedUseCase.run()
        applicationScope.launch(Dispatchers.IO) {
            appOpenedUseCase.preload()
        }

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
        systemNotificationManager.createChannel(
            "notify_sprint",
            R.string.notification_channel_sprint_notify
        )
        applicationScope.launch(Dispatchers.IO) {
            when (notificationRepository.notificationNotifyQualifying) {
                true -> remoteNotificationSubscribeUseCase.subscribe("notify_qualifying")
                false -> remoteNotificationUnsubscribeUseCase.unsubscribe("notify_qualifying")
            }
            when (notificationRepository.notificationNotifySprint) {
                true -> remoteNotificationSubscribeUseCase.subscribe("notify_sprint")
                false -> remoteNotificationUnsubscribeUseCase.unsubscribe("notify_sprint")
            }
            when (notificationRepository.notificationNotifyRace) {
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
            if (hasWidgetsUseCase.hasWidgets()) "true" else "false"
        )
        analyticsManager.setUserProperty(
            DEVICE_THEME, when (themeRepository.nightMode) {
                NightMode.DAY -> "day"
                NightMode.NIGHT -> "night"
                NightMode.DEFAULT -> if (application.isInDayMode()) "day (default)" else "night (default)"
            }
        )

        // Update Widgets
        updateWidgetsUseCase.update()
    }
}
