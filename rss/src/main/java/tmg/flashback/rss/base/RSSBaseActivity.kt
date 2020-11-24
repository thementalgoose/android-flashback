package tmg.flashback.rss.base

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.koin.android.ext.android.inject
import tmg.flashback.rss.R
import tmg.flashback.rss.prefs.RSSPrefsDB
import tmg.utilities.lifecycle.common.CommonActivity

abstract class RSSBaseActivity: CommonActivity() {

    val prefsDB: RSSPrefsDB by inject()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        window.statusBarColor = Color.TRANSPARENT

        window.apply {
            navigationBarColor = Color.TRANSPARENT
//            if (isLightTheme) {
//                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            } else {
//                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//            }
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