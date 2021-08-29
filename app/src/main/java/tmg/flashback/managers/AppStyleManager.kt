package tmg.flashback.managers

import tmg.core.ui.managers.StyleManager
import tmg.core.ui.model.NightMode
import tmg.flashback.R

class AppStyleManager: StyleManager {
    override fun getStyleResource(nightMode: NightMode): Int {
        return when (nightMode) {
            NightMode.DEFAULT -> R.style.FlashbackAppTheme_MaterialYou
            NightMode.DAY -> R.style.FlashbackAppTheme_MaterialYou
            NightMode.NIGHT -> R.style.FlashbackAppTheme_MaterialYou
        }
    }
}