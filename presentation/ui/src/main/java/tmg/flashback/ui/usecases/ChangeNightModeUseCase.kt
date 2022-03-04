package tmg.flashback.ui.usecases

import androidx.appcompat.app.AppCompatDelegate
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.repository.ThemeRepository

class ChangeNightModeUseCase(
    private val themeRepository: ThemeRepository
) {

    fun setNightMode(nightMode: NightMode) {
        themeRepository.nightMode = nightMode
        when (nightMode) {
            NightMode.DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            NightMode.DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            NightMode.NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}