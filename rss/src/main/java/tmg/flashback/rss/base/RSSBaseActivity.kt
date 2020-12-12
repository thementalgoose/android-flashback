package tmg.flashback.rss.base

import android.os.Bundle
import androidx.annotation.StyleRes
import org.koin.android.ext.android.inject
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.rss.R
import tmg.flashback.rss.prefs.RSSPrefsRepository
import tmg.utilities.extensions.isInDayMode
import tmg.utilities.lifecycle.common.CommonActivity

abstract class RSSBaseActivity: CommonActivity() {

    val prefsRepository: RSSPrefsRepository by inject()

    private var isLightTheme: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getThemeStyle())
        super.onCreate(savedInstanceState)
    }

    @StyleRes
    private fun getThemeStyle(): Int {
        isLightTheme = prefsRepository.theme == ThemePref.DAY || (prefsRepository.theme == ThemePref.AUTO && isInDayMode())
        return when (isLightTheme) {
            true -> R.style.RSS_LightTheme
            false -> R.style.RSS_DarkTheme
        }
    }
}