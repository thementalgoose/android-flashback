package tmg.flashback

import android.app.Application
import com.github.stkent.bugshaker.BugShaker
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tmg.flashback.di.firebaseModule
import tmg.flashback.di.flashbackModule
import tmg.flashback.di.rssModule
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.repo.db.PrefsDB

val releaseNotes: Map<Int, Int> = mapOf(
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

    private val prefs: PrefsDB by inject()

    private val crashManager: CrashManager by inject()

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
        if (prefs.shakeToReport) {
            BugShaker.get(this)
                .setEmailAddresses("thementalgoose@gmail.com")
                .setEmailSubjectLine("${getString(R.string.app_name)} - App Feedback")
                .setAlertDialogType(AlertDialogType.APP_COMPAT)
                .setLoggingEnabled(BuildConfig.DEBUG)
                .assemble()
                .start()
        }

        // Crash Reporting
        crashManager.initialise()
    }
}