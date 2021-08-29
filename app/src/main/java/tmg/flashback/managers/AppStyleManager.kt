package tmg.flashback.managers

import android.content.Context
import tmg.core.ui.managers.StyleManager
import tmg.core.ui.model.NightMode
import tmg.core.ui.model.Theme
import tmg.flashback.R

class AppStyleManager: StyleManager {
    override fun getStyleResource(theme: Theme, nightMode: NightMode): Int {
        return when (theme) {
            Theme.DEFAULT -> R.style.FlashbackAppTheme_Default
            Theme.MATERIAL_YOU -> R.style.FlashbackAppTheme_MaterialYou
        }
    }
}