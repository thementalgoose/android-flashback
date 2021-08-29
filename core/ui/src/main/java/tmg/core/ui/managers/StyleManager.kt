package tmg.core.ui.managers

import androidx.annotation.StyleRes
import tmg.core.ui.model.NightMode

interface StyleManager {
    @StyleRes
    fun getStyleResource(nightMode: NightMode): Int
}