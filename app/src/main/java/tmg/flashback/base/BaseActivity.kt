package tmg.flashback.base

import android.os.Bundle
import android.util.Log
import org.koin.android.ext.android.inject
import tmg.flashback.BuildConfig
import tmg.flashback.R
import tmg.flashback.repo.db.CrashReporter
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.ThemePref
import tmg.utilities.lifecycle.common.CommonActivity

abstract class BaseActivity : CommonActivity() {

    private val prefsDB: PrefsDB by inject()

    val crashReporter: CrashReporter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        when (prefsDB.theme) {
            ThemePref.DAY -> setTheme(R.style.LightTheme)
            ThemePref.AUTO -> setTheme(R.style.LightTheme)
            ThemePref.NIGHT -> setTheme(R.style.LightTheme)
        }
        super.onCreate(savedInstanceState)
    }
}
