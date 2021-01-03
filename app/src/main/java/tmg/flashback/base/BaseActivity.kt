package tmg.flashback.base

import android.os.Bundle
import androidx.annotation.StyleRes
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrInterface
import org.koin.android.ext.android.inject
import tmg.flashback.R
import tmg.flashback.extensions.isLightMode
import tmg.flashback.repo.pref.UserRepository
import tmg.utilities.lifecycle.common.CommonActivity

abstract class BaseActivity : CommonActivity() {

    private val prefsRepository: UserRepository by inject()

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
        }
    }

    @StyleRes
    private fun getThemeStyle(): Int {
        isLightTheme = prefsRepository.theme.isLightMode(this)
        return when (isLightTheme) {
            true -> R.style.LightTheme
            false -> R.style.DarkTheme
        }
    }
}
