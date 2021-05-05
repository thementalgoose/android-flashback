package tmg.flashback.shared.ui.controllers

import android.content.Context
import androidx.annotation.StyleRes
import tmg.flashback.shared.ui.model.AnimationSpeed
import tmg.flashback.shared.ui.model.Theme
import tmg.utilities.extensions.isInDayMode

abstract class ThemeController(
    private val applicationContext: Context
) {

    /**
     * Animation speed preference
     */
    abstract var animationSpeed: AnimationSpeed

    /**
     * Theme preference
     */
    abstract var theme: Theme

    @get:StyleRes
    val themeStyle: Int
        get() {
            return when (theme) {
                Theme.DEFAULT -> if (applicationContext.isInDayMode()) {
                    getStyleResource(Theme.DAY)
                } else {
                    getStyleResource(Theme.NIGHT)
                }
                else -> getStyleResource(theme)
            }
        }

    @StyleRes
    protected abstract fun getStyleResource(theme: Theme): Int
}