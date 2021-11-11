package tmg.flashback.ui.base

import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.ui.controllers.ThemeController
import tmg.flashback.ui.model.DisplayType

abstract class BaseActivity : AppCompatActivity() {

    private val themeController: ThemeController by inject()
    protected val analyticsManager: AnalyticsManager by inject()

    /**
     * Should we use the translucent variant of the theme or not
     */
    open val themeType: DisplayType = DisplayType.TRANSLUCENT

    /**
     * Override the swipe dismiss activity
     */
    var swipeDismissLock: Boolean = false
//        set(value) {
//            field = value
//            when (value) {
//                true -> swipeDismissInterface?.lock()
//                false -> swipeDismissInterface?.unlock()
//            }
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(themeRes)
        super.onCreate(savedInstanceState)
    }

    protected val themeRes: Int
        @StyleRes
        get() = themeController.themeStyle

    /**
     * Logging screen analytics
     */
    fun logScreenViewed(name: String, params: Map<String, String> = mapOf()) {
        analyticsManager.viewScreen(name, this::class.java, params)
    }
}