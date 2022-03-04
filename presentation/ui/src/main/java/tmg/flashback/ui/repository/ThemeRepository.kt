package tmg.flashback.ui.repository

import androidx.appcompat.app.AppCompatDelegate
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.ui.controllers.ThemeController
import tmg.flashback.ui.model.NightMode
import tmg.utilities.extensions.toEnum

class ThemeRepository(
    private val preferenceManager: PreferenceManager
) {
    companion object {
        private const val keyNightMode: String = "THEME" // Used to be theme pref
        private const val keyTheme: String = "THEME_CHOICE" //
        private const val keyAnimationSpeed: String = "BAR_ANIMATION"

        private const val keyMaterialYou: String = "material_you"
    }

    /**
     * Night Mode preference
     */
    var nightMode: NightMode
        get() = preferenceManager.getString(keyNightMode)?.toEnum<NightMode> { it.key } ?: NightMode.DEFAULT
        internal set(value) {
            preferenceManager.save(keyNightMode, value.key)
        }
}