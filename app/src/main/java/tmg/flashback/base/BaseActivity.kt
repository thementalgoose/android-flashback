package tmg.flashback.base

import android.os.Bundle
import androidx.annotation.StyleRes
import org.koin.android.ext.android.inject
import tmg.flashback.R
import tmg.flashback.repo.config.ToggleRepository
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.repo.pref.PrefCustomisationRepository
import tmg.flashback.repo.enums.ThemePref
import tmg.utilities.extensions.isInDayMode
import tmg.utilities.lifecycle.common.CommonActivity

abstract class BaseActivity : CommonActivity() {

    private val prefsRepository: PrefCustomisationRepository by inject()

    val crashManager: CrashManager by inject()
    val toggleDB: ToggleRepository by inject()

    protected var isLightTheme: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getThemeStyle())

        super.onCreate(savedInstanceState)
    }

    @StyleRes
    private fun getThemeStyle(): Int {
        isLightTheme = prefsRepository.theme == ThemePref.DAY || (prefsRepository.theme == ThemePref.AUTO && isInDayMode())
        return when (isLightTheme) {
            true -> R.style.LightTheme
            false -> R.style.DarkTheme
        }
    }
}
