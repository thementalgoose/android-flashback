package tmg.flashback.base

import android.os.Bundle
import androidx.annotation.StyleRes
import org.koin.android.ext.android.inject
import tmg.flashback.R
import tmg.flashback.repo.toggle.ToggleDB
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.repo.pref.PrefCustomisationDB
import tmg.flashback.repo.enums.ThemePref
import tmg.utilities.extensions.isInDayMode
import tmg.utilities.lifecycle.common.CommonActivity

abstract class BaseActivity : CommonActivity() {

    private val prefsDB: PrefCustomisationDB by inject()

    val crashManager: CrashManager by inject()
    val toggleDB: ToggleDB by inject()

    protected var isLightTheme: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getThemeStyle())

        super.onCreate(savedInstanceState)
    }

    @StyleRes
    private fun getThemeStyle(): Int {
        isLightTheme = prefsDB.theme == ThemePref.DAY || (prefsDB.theme == ThemePref.AUTO && isInDayMode())
        return when (isLightTheme) {
            true -> R.style.LightTheme
            false -> R.style.DarkTheme
        }
    }
}
