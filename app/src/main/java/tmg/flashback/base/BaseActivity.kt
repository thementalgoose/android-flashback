package tmg.flashback.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.StyleRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.koin.android.ext.android.inject
import tmg.flashback.R
import tmg.flashback.repo.ToggleDB
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.ThemePref
import tmg.utilities.extensions.isInDayMode
import tmg.utilities.lifecycle.common.CommonActivity

abstract class BaseActivity : CommonActivity() {

    private val prefsDB: PrefsDB by inject()

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
