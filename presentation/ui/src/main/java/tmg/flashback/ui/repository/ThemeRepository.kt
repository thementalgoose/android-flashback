package tmg.flashback.ui.repository

import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.model.Theme
import tmg.utilities.extensions.toEnum
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepository @Inject constructor(
    private val preferenceManager: PreferenceManager
) {
    companion object {
        private const val keyNightMode: String = "THEME" // Used to be theme pref
        private const val keyTheme: String = "THEME_CHOICE" //
    }


    /**
     * Night Mode preference
     */
    var nightMode: NightMode
        get() = preferenceManager.getString(keyNightMode)?.toEnum<NightMode> { it.key }
            ?: NightMode.DEFAULT
        internal set(value) {
            preferenceManager.save(keyNightMode, value.key)
        }

    /**
     * Theme preference
     */
    var theme: Theme
        get() = preferenceManager.getString(keyTheme)?.toEnum<Theme> { it.key } ?: Theme.DEFAULT
        set(value) = preferenceManager.save(keyTheme, value.key)
}