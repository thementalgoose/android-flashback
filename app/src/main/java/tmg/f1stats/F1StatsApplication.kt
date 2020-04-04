package tmg.f1stats

import android.app.Application
import com.github.stkent.bugshaker.BugShaker
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tmg.f1stats.di.f1Module
import tmg.f1stats.prefs.SharedPrefsDB
import tmg.f1stats.repo.db.PrefsDB

class F1StatsApplication: Application() {

    private val prefs: PrefsDB by inject()

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@F1StatsApplication)
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
    }
}