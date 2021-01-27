package tmg.flashback.rss.base

import android.os.Bundle
import androidx.annotation.StyleRes
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrInterface
import org.koin.android.ext.android.inject
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.rss.R
import tmg.flashback.rss.managers.RSSAnalyticsManager
import tmg.flashback.rss.prefs.RSSPrefsRepository
import tmg.utilities.extensions.isInDayMode
import tmg.utilities.lifecycle.common.CommonActivity
import java.lang.NullPointerException

abstract class RSSBaseActivity: CommonActivity() {
    /**
     * Custom attributes to be added to the screen view analytics
     */
    open val analyticsCustomAttributes: Map<String, String> = emptyMap()

    /**
     * Custom screen name to be added to the screen view metric
     */
    open val analyticsScreenName: String = this.javaClass.simpleName

    val prefsRepository: RSSPrefsRepository by inject()
    val analyticsManager: RSSAnalyticsManager by inject()

    private var isLightTheme: Boolean = true

    //region Slidr

    private var swipeDismissInterface: SlidrInterface? = null
    open val slidrInit: Boolean = true
    var slidrLock: Boolean = false
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

        if (slidrInit) {
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
        if (analyticsManager.enableAnalytics) {
            val analyticsAttributes = try {
                analyticsCustomAttributes
            } catch (e: NullPointerException) {
                emptyMap()
            }
            analyticsManager.rssViewScreen(
                    screenName = analyticsScreenName,
                    clazz = this.javaClass,
                    mapOfParams = analyticsAttributes
            )
        }
    }

    @StyleRes
    private fun getThemeStyle(): Int {
        isLightTheme = prefsRepository.theme == ThemePref.DAY || (prefsRepository.theme == ThemePref.AUTO && isInDayMode())
        return when (isLightTheme) {
            true -> R.style.RSS_LightTheme
            false -> R.style.RSS_DarkTheme
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
    }
}