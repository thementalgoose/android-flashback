package tmg.flashback.controllers

import android.content.Context
import tmg.flashback.R
import tmg.flashback.constants.Defaults
import tmg.flashback.device.repository.SharedPreferenceRepository
import tmg.flashback.shared.ui.controllers.ThemeController
import tmg.flashback.shared.ui.model.AnimationSpeed
import tmg.flashback.shared.ui.model.Theme
import tmg.utilities.extensions.toEnum

class AppThemeController(
    applicationContext: Context,
    private val sharedPreferenceRepository: SharedPreferenceRepository
): ThemeController(applicationContext) {

    companion object {
        private const val keyTheme: String = "THEME"
        private const val keyAnimationSpeed: String = "BAR_ANIMATION"
    }

    override var theme: Theme
        get() = sharedPreferenceRepository.getString(keyTheme)?.toEnum<Theme> { it.key } ?: Theme.DEFAULT
        set(value) = sharedPreferenceRepository.save(keyTheme, value.key)

    override var animationSpeed: AnimationSpeed
        get() = sharedPreferenceRepository.getString(keyAnimationSpeed)?.toEnum<AnimationSpeed> { it.key } ?: AnimationSpeed.MEDIUM
        set(value) = sharedPreferenceRepository.save(keyAnimationSpeed, value.key)

    public override fun getStyleResource(theme: Theme): Int {
        return when (theme) {
            Theme.DEFAULT -> 0
            Theme.DAY -> R.style.DayTheme
            Theme.NIGHT -> R.style.DayTheme
        }
    }
}