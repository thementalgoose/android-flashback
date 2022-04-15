package tmg.flashback.ui.base

import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.DisplayType
import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.ui.repository.ThemeRepository

abstract class BaseActivity : AppCompatActivity() {

    private val styleManager: StyleManager by inject()
    private val activityProvider: ActivityProvider by inject()
    protected val analyticsManager: AnalyticsManager by inject()

    /**
     * Should we use the translucent variant of the theme or not
     */
    open val themeType: DisplayType = DisplayType.TRANSLUCENT

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(themeRes)
        super.onCreate(savedInstanceState)
    }

    protected val themeRes: Int
        @StyleRes
        get() = styleManager.getStyleResource()

    /**
     * Logging screen analytics
     */
    fun logScreenViewed(name: String, params: Map<String, String> = mapOf()) {
        analyticsManager.viewScreen(name, this::class.java, params)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            activityProvider.onWindowFocusObtained(this)
        }
    }
}