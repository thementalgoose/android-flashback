package tmg.flashback.controllers

import android.content.Context
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.repo.pref.UserRepository
import tmg.utilities.extensions.isInDayMode

/**
 * Controller for any appearance preferences that could have been
 *   configured in the settings of the app
 */
class AppearanceController(
        private val userRepository: UserRepository,
        private val applicationContext: Context
) {

    //region Theme

    var currentTheme: ThemePref
        get() = userRepository.theme
        set(value) {
            userRepository.theme = value
        }

    val isLightMode: Boolean
        get() = currentTheme == ThemePref.DAY || (currentTheme == ThemePref.AUTO && applicationContext.isInDayMode())

    //endregion

    //region Bar Animation speed

    var barAnimation: BarAnimation
        get() = userRepository.barAnimation
        set(value) {
            userRepository.barAnimation = value
        }

    //endregion

}