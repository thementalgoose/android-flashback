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
import tmg.flashback.strings.R.string
import tmg.flashback.ads.ads.usecases.InitialiseAdsUseCase
import tmg.flashback.googleanalytics.UserProperty.DEVICE_MODEL
import tmg.flashback.googleanalytics.UserProperty.DEVICE_THEME
import tmg.flashback.googleanalytics.UserProperty.OS_VERSION
import tmg.flashback.googleanalytics.UserProperty.WIDGET_USAGE
import tmg.flashback.googleanalytics.manager.FirebaseAnalyticsManager
import tmg.flashback.configuration.usecases.InitialiseConfigUseCase
import tmg.flashback.crashlytics.model.FirebaseKey
import tmg.flashback.crashlytics.services.FirebaseCrashService
import tmg.flashback.crashlytics.usecases.InitialiseCrashReportingUseCase
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.device.repository.PrivacyRepository
import tmg.flashback.device.usecases.AppOpenedUseCase
import tmg.flashback.device.usecases.GetDeviceInfoUseCase
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.googleanalytics.UserProperty.APP_VERSION
import tmg.flashback.googleanalytics.UserProperty.DEVICE_BOARD
import tmg.flashback.googleanalytics.UserProperty.DEVICE_BRAND
import tmg.flashback.googleanalytics.UserProperty.DEVICE_MANUFACTURER
import tmg.flashback.notifications.managers.SystemNotificationManager
import tmg.flashback.notifications.usecases.RemoteNotificationSubscribeUseCase
import tmg.flashback.notifications.usecases.RemoteNotificationUnsubscribeUseCase
import tmg.flashback.repositories.ContactRepository
import tmg.flashback.season.contract.repository.NotificationsRepository
import tmg.flashback.season.contract.repository.models.NotificationResultsAvailable
import tmg.flashback.season.contract.repository.models.NotificationUpcoming
import tmg.flashback.season.repository.HomeRepository
import tmg.flashback.style.AppTheme
import tmg.flashback.style.SupportedTheme
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.model.Theme
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.usecases.RefreshWidgetsUseCase
import tmg.flashback.widgets.upnext.contract.usecases.HasUpNextWidgetsUseCase
import tmg.utilities.extensions.format
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
    private val notificationRepository: NotificationsRepository,
    private val themeRepository: ThemeRepository,
    private val buildConfigManager: BuildConfigManager,
    private val firebaseAnalyticsManager: FirebaseAnalyticsManager,
    private val firebaseCrashService: FirebaseCrashService,
    private val initialiseConfigUseCase: InitialiseConfigUseCase,
    private val systemNotificationManager: SystemNotificationManager,
    private val remoteNotificationSubscribeUseCase: RemoteNotificationSubscribeUseCase,
    private val remoteNotificationUnsubscribeUseCase: RemoteNotificationUnsubscribeUseCase,
    private val appOpenedUseCase: AppOpenedUseCase,
    private val getDeviceInfoUseCase: GetDeviceInfoUseCase,
    private val initialiseAdsUseCase: InitialiseAdsUseCase,
    private val refreshWidgetsUseCase: RefreshWidgetsUseCase,
    private val homeRepository: HomeRepository,
    private val hasUpNextWidgetsUseCase: HasUpNextWidgetsUseCase,
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

        // App evnts
        if (deviceRepository.appOpenedCount == 0) {
            onFirstRun()
        } else if (deviceRepository.lastAppVersion != BuildConfig.VERSION_CODE) {
            onAppUpgrade(deviceRepository.lastAppVersion)
        }

        // App Startup
        appOpenedUseCase.run()

        // Crash Reporting
        initialiseCrashReportingUseCase.initialise(
            deviceUuid = deviceRepository.deviceUdid,
            extraKeys = mapOf(
                FirebaseKey.AppOpenCount to deviceRepository.appOpenedCount.toString(),
                FirebaseKey.AppFirstOpen to (deviceRepository.appFirstOpened.format("dd MMM yyyy") ?: "-"),
                FirebaseKey.WidgetCount to if (hasUpNextWidgetsUseCase.hasWidgets()) "true" else "false",
            )
        )

        // Adverts
        initialiseAdsUseCase.initialise()

        //region Notifications Legacy: Remove these existing channels which were previously used for remote notifications
        systemNotificationManager.cancelChannel("race")
        systemNotificationManager.cancelChannel("qualifying")
        //endregion

        // Notifications
        val notificationUpcoming = "upcoming"
        systemNotificationManager.createGroup(notificationUpcoming, application.getString(string.notification_channel_title_upcoming))
        NotificationUpcoming.entries.filter { it != NotificationUpcoming.OTHER }.forEach {
            systemNotificationManager.createChannel(
                it.channelId,
                it.channelLabel,
                groupId = notificationUpcoming
            )
        }

        systemNotificationManager.createChannel(
            NotificationUpcoming.OTHER.channelId,
            NotificationUpcoming.OTHER.channelLabel,
            groupId = null
        )

        val notificationResultIds = "results"
        systemNotificationManager.createGroup(notificationResultIds, application.getString(string.notification_channel_title_results))
        NotificationResultsAvailable.entries.forEach { results ->
            systemNotificationManager.createChannel(
                results.channelId,
                results.channelLabel,
                groupId = notificationResultIds
            )
        }
        applicationScope.launch(Dispatchers.IO) {
            NotificationResultsAvailable.entries.forEach { result ->
                when (notificationRepository.isEnabled(result)) {
                    true -> remoteNotificationSubscribeUseCase.subscribe(result.remoteSubscriptionTopic)
                    false -> remoteNotificationUnsubscribeUseCase.unsubscribe(result.remoteSubscriptionTopic)
                }
            }
        }

        // Set viewed seasons
        val currentYear = Formula1.currentSeasonYear - 1
        val supportedSeasons = homeRepository.supportedSeasons.filter { it <= currentYear }
        homeRepository.viewedSeasons += supportedSeasons

        // Initialise user properties
        firebaseAnalyticsManager.initialise(userId = deviceRepository.deviceUdid)
        firebaseAnalyticsManager.setUserProperty(APP_VERSION, buildConfigManager.versionName)
        firebaseAnalyticsManager.setUserProperty(OS_VERSION, Build.VERSION.SDK_INT.toString())
        firebaseAnalyticsManager.setUserProperty(DEVICE_BOARD, Build.BOARD)
        firebaseAnalyticsManager.setUserProperty(DEVICE_MODEL, Build.MODEL)
        firebaseAnalyticsManager.setUserProperty(DEVICE_BRAND, Build.BRAND)
        firebaseAnalyticsManager.setUserProperty(DEVICE_MANUFACTURER, Build.MANUFACTURER)
        firebaseAnalyticsManager.setUserProperty(WIDGET_USAGE, if (hasUpNextWidgetsUseCase.hasWidgets()) "true" else "false")
        firebaseAnalyticsManager.setUserProperty(
            DEVICE_THEME, when (themeRepository.nightMode) {
                NightMode.DAY -> "day"
                NightMode.NIGHT -> "night"
                NightMode.DEFAULT -> if (application.isInDayMode()) "day (default)" else "night (default)"
            }
        )

        applicationScope.launch(Dispatchers.IO) {
            appOpenedUseCase.preload()
            firebaseCrashService.setCustomKey(FirebaseKey.InstallationId, deviceRepository.installationId)
        }

        // Update Widgets
        refreshWidgetsUseCase.update()
    }

    private fun onFirstRun() {
        // Do something here only when the app is first setup
    }

    private fun onAppUpgrade(previousAppVersion: Int) {
        when {
            previousAppVersion < 0 -> { }
        }
    }
}
