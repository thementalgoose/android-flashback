package tmg.flashback

import android.app.Application
import android.os.Build
import android.util.Log
import com.github.stkent.bugshaker.BugShaker
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tmg.flashback.controllers.DeviceController
import tmg.flashback.controllers.NotificationController
import tmg.flashback.managers.analytics.UserPropertiesManager
import tmg.flashback.di.*
import tmg.flashback.notifications.PushNotificationManager
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.repo.pref.DeviceRepository

val releaseNotes: Map<Int, Int> = mapOf(
    40 to R.string.release_40,
    38 to R.string.release_38,
    36 to R.string.release_36,
    34 to R.string.release_34,
    33 to R.string.release_33,
    32 to R.string.release_32,
    31 to R.string.release_31,
    30 to R.string.release_30,
    28 to R.string.release_28,
    27 to R.string.release_27,
    26 to R.string.release_26,
    25 to R.string.release_25,
    24 to R.string.release_24,
    23 to R.string.release_23,
    22 to R.string.release_22,
    21 to R.string.release_21,
    20 to R.string.release_20,
    18 to R.string.release_18,
    16 to R.string.release_16,
    15 to R.string.release_15,
    14 to R.string.release_14,
    13 to R.string.release_13,
    10 to R.string.release_10,
    8 to R.string.release_8,
    7 to R.string.release_7,
    6 to R.string.release_6,
    4 to R.string.release_4,
    2 to R.string.release_2,
    1 to R.string.release_1
)

class FlashbackApplication: Application() {

    private val deviceController: DeviceController by inject()
    private val prefsNotification: NotificationController by inject()

    private val configRepository: RemoteConfigRepository by inject()
    private val crashManager: CrashManager by inject()
    private val analyticsUserProperties: UserPropertiesManager by inject()

    private val notificationManager: PushNotificationManager by inject()

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@FlashbackApplication)
            modules(
                configurationModule,
                deviceModule,
                firebaseModule,
                shortcutModule,
                viewModelModule,
                analyticsModule
            )
            modules(
                rssModule,
                rssViewModelModule
            )
        }

        // ThreeTen
        AndroidThreeTen.init(this)

        // Shake to report a bug
        if (deviceController.shakeToReport) {
            BugShaker.get(this)
                .setEmailAddresses("thementalgoose@gmail.com")
                .setEmailSubjectLine("${getString(R.string.app_name)} - App Feedback")
                .setAlertDialogType(AlertDialogType.APP_COMPAT)
                .setLoggingEnabled(BuildConfig.DEBUG)
                .assemble()
                .start()
        }

        // App bootup stats

        deviceController.appFirstBoot
        deviceController.appOpened()
        if (BuildConfig.DEBUG) {
            Log.i("Flashback", "First boot time ${deviceController.appFirstBoot}")
            Log.i("Flashback", "App open count ${deviceController.appOpenedCount}")
        }

        // Crash Reporting
        crashManager.initialise(
            appFirstOpened = deviceController.appFirstBoot.toString(),
            appOpenedCount = deviceController.appOpenedCount
        )

        // Remote config
        GlobalScope.launch {
            val result = configRepository.update(false)
            Log.i("Flashback", "Remote config updated $result")
        }

        // Channels
        notificationManager.createChannels()

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

        // Initialise user properties
        analyticsUserProperties.setDeviceModel(Build.MODEL)
        analyticsUserProperties.setOsVersion(Build.VERSION.SDK_INT.toString())
    }
}