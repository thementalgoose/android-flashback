package tmg.flashback.base

import android.graphics.Color
import android.graphics.Insets
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        window.statusBarColor = Color.TRANSPARENT

        window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        findViewById<View>(R.id.container)?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { view, insets ->
                setInsets(insets = insets)
                insets
            }
        }
    }

    open fun setInsets(insets: WindowInsetsCompat) {

    }
}
