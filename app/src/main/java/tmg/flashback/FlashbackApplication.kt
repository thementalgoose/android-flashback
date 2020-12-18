package tmg.flashback

import android.app.Application
import android.util.Log
import com.github.stkent.bugshaker.BugShaker
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tmg.flashback.di.firebaseModule
import tmg.flashback.di.flashbackModule
import tmg.flashback.di.rssModule
import tmg.flashback.notifications.PushNotificationManager
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.repo.pref.PrefDeviceRepository
import tmg.flashback.repo.pref.PrefNotificationRepository

val releaseNotes: Map<Int, Int> = mapOf(
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

    private val prefsDevice: PrefDeviceRepository by inject()
    private val prefsNotification: PrefNotificationRepository by inject()

    private val configRepository: RemoteConfigRepository by inject()
    private val crashManager: CrashManager by inject()

    private val notificationManager: PushNotificationManager by inject()

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@FlashbackApplication)
            modules(flashbackModule, rssModule, firebaseModule)
        }

        // ThreeTen
        AndroidThreeTen.init(this)

        // Shake to report a bug
        if (prefsDevice.shakeToReport) {
            BugShaker.get(this)
                .setEmailAddresses("thementalgoose@gmail.com")
                .setEmailSubjectLine("${getString(R.string.app_name)} - App Feedback")
                .setAlertDialogType(AlertDialogType.APP_COMPAT)
                .setLoggingEnabled(BuildConfig.DEBUG)
                .assemble()
                .start()
        }

        // App bootup stats
        prefsDevice.appFirstBootTime
        prefsDevice.appOpenedCount = prefsDevice.appOpenedCount + 1
        if (BuildConfig.DEBUG) {
            Log.i("Flashback", "First boot time ${prefsDevice.appFirstBootTime}")
            Log.i("Flashback", "App open count ${prefsDevice.appOpenedCount}")
        }

        // Crash Reporting
        crashManager.initialise(
            appFirstOpened = prefsDevice.appFirstBootTime.toString(),
            appOpenedCount = prefsDevice.appOpenedCount
        )

        // Remote config
        GlobalScope.launch {
            val result = configRepository.update(false)
            Log.i("Flashback", "Remote config updated $result")
        }

        // Channels
        notificationManager.createChannels()

        // Enrol for race push notifications
        if (prefsNotification.notificationsRace == null) {
            GlobalScope.launch {
                val result = notificationManager.raceSubscribe()
                Log.i("Flashback", "Auto enrol push notifications race - $result")
            }
        }

        // Enrol for qualifying push notifications
        if (prefsNotification.notificationsQualifying == null) {
            GlobalScope.launch {
                val result = notificationManager.qualifyingSubscribe()
                Log.i("Flashback", "Auto enrol push notifications qualifying - $result")
            }
        }

        // Enrol for qualifying push notifications
        if (prefsNotification.notificationsMisc == null) {
            GlobalScope.launch {
                val result = notificationManager.appSupportSubscribe()
                Log.i("Flashback", "Auto enrol push notifications misc - $result")
            }
        }
    }
}