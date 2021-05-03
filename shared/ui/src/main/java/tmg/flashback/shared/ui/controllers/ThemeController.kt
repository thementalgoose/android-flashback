package tmg.flashback.shared.ui.controllers

import android.content.Context
import androidx.annotation.StyleRes
import tmg.flashback.shared.ui.model.Theme
import tmg.utilities.extensions.isInDayMode

abstract class ThemeController(
    private val applicationContext: Context
) {

    protected abstract var themePref: Theme

    @get:StyleRes
    val themeStyle: Int
        get() {
            return when (themePref) {
                Theme.DEFAULT -> if (applicationContext.isInDayMode()) {
                    getStyleResource(Theme.DAY)
                } else {
                    getStyleResource(Theme.NIGHT)
                }
                else -> getStyleResource(themePref)
            }
        }

    @StyleRes
    abstract fun getStyleResource(theme: Theme): Int
}