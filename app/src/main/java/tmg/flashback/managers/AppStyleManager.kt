package tmg.flashback.managers

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.R
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.model.Theme
import tmg.flashback.ui.repository.ThemeRepository
import tmg.utilities.extensions.isInDayMode
import javax.inject.Inject

class AppStyleManager @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context,
    private val themeRepository: ThemeRepository
): StyleManager {

    override fun getStyleResource() = getStyleResource(
        theme = themeRepository.theme,
        nightMode = themeRepository.nightMode
    )

    override fun getStyleResource(theme: Theme, nightMode: NightMode): Int {
        return when (theme) {
            Theme.DEFAULT -> R.style.FlashbackAppTheme_Default
            Theme.MATERIAL_YOU -> R.style.FlashbackAppTheme_MaterialYou
        }
    }

    override val isDayMode: Boolean
        get() = when (themeRepository.nightMode) {
            NightMode.DEFAULT -> applicationContext.isInDayMode()
            NightMode.DAY -> true
            NightMode.NIGHT -> false
        }
}