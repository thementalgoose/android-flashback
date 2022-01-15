package tmg.flashback.managers

import tmg.flashback.R
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.model.Theme

class AppStyleManager: StyleManager {
    override fun getStyleResource(theme: Theme, nightMode: NightMode): Int {
        return when (theme) {
            Theme.DEFAULT -> R.style.FlashbackAppTheme_Default
            Theme.MATERIAL_YOU -> R.style.FlashbackAppTheme_MaterialYou
        }
    }
}