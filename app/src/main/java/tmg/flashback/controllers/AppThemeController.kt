package tmg.flashback.controllers

import android.content.Context
import tmg.flashback.R
import tmg.flashback.constants.Defaults
import tmg.flashback.device.repository.SharedPreferenceRepository
import tmg.flashback.shared.ui.controllers.ThemeController
import tmg.flashback.shared.ui.model.Theme
import tmg.utilities.extensions.toEnum

class AppThemeController(
    applicationContext: Context,
    private val sharedPreferenceRepository: SharedPreferenceRepository
): ThemeController(applicationContext) {

    companion object {
        private const val keyTheme: String = "THEME"
    }

    override var themePref: Theme
        get() = sharedPreferenceRepository.getString(keyTheme)?.toEnum<Theme> { it.key } ?: Theme.DEFAULT
        set(value) = sharedPreferenceRepository.save(keyTheme, value.key)

    override fun getStyleResource(theme: Theme): Int {
        return when (theme) {
            Theme.DEFAULT -> 0
            Theme.DAY -> R.style.LightTheme
            Theme.NIGHT -> R.style.DarkTheme
        }
    }
}