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

        window.statusBarColor = Color.TRANSPARENT

        window.apply {
            navigationBarColor = Color.TRANSPARENT
            if (isLightTheme) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }

        findViewById<View>(R.id.container)?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { view, insets ->
                setInsets(insets = insets)
                insets
            }
        }
    }

    @StyleRes
    private fun getThemeStyle(): Int {
        isLightTheme = prefsDB.theme == ThemePref.DAY || (prefsDB.theme == ThemePref.AUTO && isInDayMode())
        return when (isLightTheme) {
            true -> tmg.flashback.rss.R.style.RSS_LightTheme
            false -> tmg.flashback.rss.R.style.RSS_DarkTheme
        }
    }
    open fun setInsets(insets: WindowInsetsCompat) {

    }
}
