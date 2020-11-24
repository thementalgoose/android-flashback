package tmg.flashback.rss.base

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.annotation.StyleRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.koin.android.ext.android.inject
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.rss.R
import tmg.flashback.rss.prefs.RSSPrefsDB
import tmg.utilities.extensions.getColor
import tmg.utilities.extensions.isInDayMode
import tmg.utilities.lifecycle.common.CommonActivity

abstract class RSSBaseActivity: CommonActivity() {

    val prefsDB: RSSPrefsDB by inject()

    private var isLightTheme: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getThemeStyle())
        super.onCreate(savedInstanceState)
    }

    @StyleRes
    private fun getThemeStyle(): Int {
        isLightTheme = prefsDB.theme == ThemePref.DAY || (prefsDB.theme == ThemePref.AUTO && isInDayMode())
        return when (isLightTheme) {
            true -> R.style.RSS_LightTheme
            false -> R.style.RSS_DarkTheme
        }
    }

    open fun setInsets(insets: WindowInsetsCompat) {

    }
}