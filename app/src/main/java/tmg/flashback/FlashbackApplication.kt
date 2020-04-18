package tmg.flashback

import android.app.Application
import com.github.stkent.bugshaker.BugShaker
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tmg.flashback.di.f1Module
import tmg.flashback.repo.db.CrashReporter
import tmg.flashback.repo.db.PrefsDB

val releaseNotes: Map<Int, Int> = mapOf(
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
            modules(f1Module)
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