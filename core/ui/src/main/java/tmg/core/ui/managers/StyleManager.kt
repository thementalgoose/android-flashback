package tmg.core.ui.managers

import androidx.annotation.StyleRes
import tmg.core.ui.model.Theme

interface StyleManager {
    @StyleRes
    fun getStyleResource(theme: Theme): Int
}