package tmg.flashback.core.ui

import android.os.Bundle
import androidx.annotation.StyleRes
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrInterface
import org.koin.android.ext.android.inject
import tmg.flashback.core.R
import tmg.flashback.core.controllers.AnalyticsController
import tmg.flashback.core.enums.DisplayType
import tmg.flashback.core.extensions.isLightMode
import tmg.flashback.core.repositories.CoreRepository
import tmg.utilities.lifecycle.common.CommonActivity
import java.lang.NullPointerException

abstract class BaseActivity : CommonActivity() {

    /**
     * Should we use the translucent variant of the theme or not
     */
    open val themeType: DisplayType = DisplayType.TRANSLUCENT

    /**
     * Custom attributes to be added to the screen view analytics
     */
    open val analyticsCustomAttributes: Map<String, String> = emptyMap()

    /**
     * Custom screen name to be added to the screen view metric
     */
    open val analyticsScreenName: String = this.javaClass.simpleName

    private val coreRepository: CoreRepository by inject()
    private val analyticsController: AnalyticsController by inject()

    protected var isLightTheme: Boolean = true

    //region Slidr

    private var swipeDismissInterface: SlidrInterface? = null
    open val initialiseSlidr: Boolean = true
    var swipeDismissLock: Boolean = false
        set(value) {
            field = value
            when (value) {
                true -> swipeDismissInterface?.lock()
                false -> swipeDismissInterface?.unlock()
            }
        }

    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getThemeStyle())

        super.onCreate(savedInstanceState)

        if (initialiseSlidr) {
            swipeDismissInterface = Slidr.attach(this)
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }
    }

    override fun onResume() {
        super.onResume()
        recordScreenViewed()
    }

    /**
     * Method to be ran when you want to register that a new screen has been displayed in analytics
     */
    fun recordScreenViewed() {
        val analyticsAttributes = try {
            analyticsCustomAttributes
        } catch (e: NullPointerException) {
            emptyMap()
        }
        analyticsController.viewScreen(
            screenName = analyticsScreenName,
            clazz = this.javaClass,
            params = analyticsAttributes
        )
    }

    @StyleRes
    private fun getThemeStyle(): Int {
        isLightTheme = coreRepository.theme.isLightMode(this)

        return when (isLightTheme) {
            true -> themeType.lightTheme
            false -> themeType.darkTheme
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
    }
}