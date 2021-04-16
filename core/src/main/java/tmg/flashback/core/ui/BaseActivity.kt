package tmg.flashback.core.ui

import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrInterface
import org.koin.android.ext.android.inject
import tmg.flashback.core.R
import tmg.flashback.core.controllers.AnalyticsController
import tmg.flashback.core.enums.DisplayType
import tmg.flashback.core.extensions.isLightMode
import tmg.flashback.core.repositories.CoreRepository
import tmg.flashback.core.utils.ScreenAnalytics

abstract class BaseActivity : AppCompatActivity() {

    protected val analyticsController: AnalyticsController by inject()
    private val coreRepository: CoreRepository by inject()
    private var swipeDismissInterface: SlidrInterface? = null

    /**
     * Should we use the translucent variant of the theme or not
     */
    open val themeType: DisplayType = DisplayType.TRANSLUCENT

    /**
     * Analytics data used in the recording of screen data
     */
    open val screenAnalytics: ScreenAnalytics? = ScreenAnalytics()

    /**
     * Should Slidr be initialised for the following activity
     */
    open val swipeDismissInitialise: Boolean = true

    /**
     * Override the swipe dismiss activity
     */
    var swipeDismissLock: Boolean = false
        set(value) {
            field = value
            when (value) {
                true -> swipeDismissInterface?.lock()
                false -> swipeDismissInterface?.unlock()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getThemeStyle())

        super.onCreate(savedInstanceState)

        if (swipeDismissInitialise) {
            swipeDismissInterface = Slidr.attach(this)
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }
    }

    override fun onResume() {
        super.onResume()
        recordScreenViewed()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
    }

    /**
     * Record that a screen has been viewed for analytics
     * @param analytics Instance of the screen analytics we will be reporting. Defaults to class level value
     */
    open fun recordScreenViewed(
        analytics: ScreenAnalytics? = screenAnalytics
    ) {
        if (analytics == null) {
            return
        }

        analyticsController.viewScreen(
            screenName = analytics.screenName ?: this.javaClass.simpleName,
            clazz = this.javaClass,
            params = analytics.attributes
        )
    }

    /**
     * Get the current theme style dependent on the device preference
     * @return style res
     */
    @StyleRes
    private fun getThemeStyle(): Int {
        return when (coreRepository.theme.isLightMode(this)) {
            true -> themeType.lightTheme
            false -> themeType.darkTheme
        }
    }
}