package tmg.flashback

import android.app.Application
import com.github.stkent.bugshaker.BugShaker
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tmg.flashback.di.flashbackModule
import tmg.flashback.repo.db.CrashReporter
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.settings.planned.PlannedItems

val releaseNotes: Map<Int, Int> = mapOf(
    1 to R.string.release_1,
    2 to R.string.release_2
)

val planned: List<PlannedItems> = listOf(
    PlannedItems(R.string.planned_0, true),
    PlannedItems(R.string.planned_1, true),
    PlannedItems(R.string.planned_2, true),
    PlannedItems(R.string.planned_3, false),
    PlannedItems(R.string.planned_4, false),
    PlannedItems(R.string.planned_5, false),
    PlannedItems(R.string.planned_6, false),
    PlannedItems(R.string.planned_7, false),
    PlannedItems(R.string.planned_8, false)
)

class FlashbackApplication: Application() {

    private val prefs: PrefsDB by inject()

    private val crashReporter: CrashReporter by inject()

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@FlashbackApplication)
            modules(flashbackModule)
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