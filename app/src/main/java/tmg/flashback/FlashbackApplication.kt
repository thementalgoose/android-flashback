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
import tmg.flashback.di.newsModule
import tmg.flashback.repo.db.CrashReporter
import tmg.flashback.repo.db.PrefsDB

val releaseNotes: Map<Int, Int> = mapOf(
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

    private val crashReporter: CrashReporter by inject()

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@FlashbackApplication)
            modules(flashbackModule, newsModule, firebaseModule)
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
        crashReporter.initialise()
    }
}