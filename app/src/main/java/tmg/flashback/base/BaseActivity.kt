package tmg.flashback.base

import android.os.Bundle
import org.koin.android.ext.android.inject
import tmg.flashback.R
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.ThemePref
import tmg.utilities.lifecycle.common.CommonActivity

abstract class BaseActivity : CommonActivity() {

    val prefsDB: PrefsDB by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        when (prefsDB.theme) {
            ThemePref.DAY -> setTheme(R.style.LightTheme)
            ThemePref.AUTO -> setTheme(R.style.LightTheme)
            ThemePref.NIGHT -> setTheme(R.style.DarkTheme)
        }
        super.onCreate(savedInstanceState)
    }
}
