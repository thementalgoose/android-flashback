package tmg.flashback.ui.managers

import androidx.annotation.StyleRes
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.model.Theme

interface StyleManager {
    @StyleRes
    fun getStyleResource(theme: Theme, nightMode: NightMode): Int
}