package tmg.core.ui.controllers

import android.content.Context
import androidx.annotation.StyleRes
import tmg.core.prefs.manager.PreferenceManager
import tmg.core.prefs.manager.SharedPreferenceManager
import tmg.core.ui.R
import tmg.core.ui.managers.StyleManager
import tmg.core.ui.model.AnimationSpeed
import tmg.core.ui.model.Theme
import tmg.utilities.extensions.isInDayMode
import tmg.utilities.extensions.toEnum

class ThemeController(
    private val applicationContext: Context,
    private val preferenceManager: PreferenceManager,
    private val styleManager: StyleManager
) {
    companion object {
        private const val keyTheme: String = "THEME"
        private const val keyAnimationSpeed: String = "BAR_ANIMATION"
    }

    /**
     * Animation speed preference
     */
    var animationSpeed: AnimationSpeed
        get() = preferenceManager.getString(keyAnimationSpeed)?.toEnum<AnimationSpeed> { it.key } ?: AnimationSpeed.MEDIUM
        set(value) = preferenceManager.save(keyAnimationSpeed, value.key)

    /**
     * Theme preference
     */
    var theme: Theme
        get() = preferenceManager.getString(keyTheme)?.toEnum<Theme> { it.key } ?: Theme.DEFAULT
        set(value) = preferenceManager.save(keyTheme, value.key)

    @get:StyleRes
    val themeStyle: Int
        get() {
            return when (theme) {
                Theme.DEFAULT -> if (applicationContext.isInDayMode()) {
                    styleManager.getStyleResource(Theme.DAY)
                } else {
                    styleManager.getStyleResource(Theme.NIGHT)
                }
                else -> styleManager.getStyleResource(theme)
            }
        }
}