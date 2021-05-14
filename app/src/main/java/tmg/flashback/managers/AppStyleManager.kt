package tmg.flashback.managers

import tmg.core.ui.managers.StyleManager
import tmg.core.ui.model.Theme
import tmg.flashback.R

class AppStyleManager: StyleManager {
    override fun getStyleResource(theme: Theme): Int {
        return when (theme) {
            Theme.DEFAULT -> R.style.FlashbackAppTheme
            Theme.DAY -> R.style.FlashbackAppTheme
            Theme.NIGHT -> R.style.FlashbackAppTheme
        }
    }
}