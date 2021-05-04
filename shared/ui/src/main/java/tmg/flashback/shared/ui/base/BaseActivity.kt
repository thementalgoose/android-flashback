package tmg.flashback.shared.ui.base

import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrInterface
import org.koin.android.ext.android.inject
import tmg.flashback.shared.ui.R
import tmg.flashback.shared.ui.controllers.ThemeController
import tmg.flashback.shared.ui.model.DisplayType

abstract class BaseActivity : AppCompatActivity() {

    private var swipeDismissInterface: SlidrInterface? = null
    private val themeController: ThemeController by inject()

    /**
     * Should we use the translucent variant of the theme or not
     */
    open val themeType: DisplayType = DisplayType.TRANSLUCENT

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
        setTheme(themeController.themeStyle)

        super.onCreate(savedInstanceState)

        if (swipeDismissInitialise) {
            swipeDismissInterface = Slidr.attach(this)
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
    }
}