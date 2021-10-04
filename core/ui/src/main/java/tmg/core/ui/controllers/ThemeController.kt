package tmg.core.ui.controllers

import android.content.Context
import androidx.annotation.StyleRes
import tmg.configuration.manager.ConfigManager
import tmg.core.prefs.manager.PreferenceManager
import tmg.core.ui.managers.StyleManager
import tmg.core.ui.model.AnimationSpeed
import tmg.core.ui.model.NightMode
import tmg.core.ui.model.Theme
import tmg.utilities.extensions.isInDayMode
import tmg.utilities.extensions.toEnum

class ThemeController(
    private val preferenceManager: PreferenceManager,
    private val configManager: ConfigManager,
    private val styleManager: StyleManager
) {
    companion object {
        private const val keyNightMode: String = "THEME" // Used to be theme pref
        private const val keyTheme: String = "THEME_CHOICE" //
        private const val keyAnimationSpeed: String = "BAR_ANIMATION"

        private const val keyMaterialYou: String = "material_you"
    }

    /**
     * Animation speed preference
     */
    var animationSpeed: AnimationSpeed
        get() = preferenceManager.getString(keyAnimationSpeed)?.toEnum<AnimationSpeed> { it.key } ?: AnimationSpeed.MEDIUM
        set(value) = preferenceManager.save(keyAnimationSpeed, value.key)

    /**
     * Night Mode preference
     */
    var nightMode: NightMode
        get() = preferenceManager.getString(keyNightMode)?.toEnum<NightMode> { it.key } ?: NightMode.DEFAULT
        set(value) = preferenceManager.save(keyNightMode, value.key)

    /**
     * Theme preference
     */
    var theme: Theme
        get() {
            if (!enableThemePicker) {
                return Theme.DEFAULT
            }
            return preferenceManager.getString(keyTheme)?.toEnum<Theme> { it.key } ?: Theme.DEFAULT
        }
        set(value) = preferenceManager.save(keyTheme, value.key)

    /**
     * Enable the theme picker for material you preference
     */
    val enableThemePicker: Boolean by lazy {
        return@lazy configManager.getBoolean(keyMaterialYou)
    }

    @get:StyleRes
    val themeStyle: Int by lazy {
        styleManager.getStyleResource(theme, nightMode)
    }
}