package tmg.flashback.core.controllers

import android.content.Context
import tmg.flashback.shared.ui.model.AnimationSpeed
import tmg.flashback.core.repositories.CoreRepository
import tmg.utilities.extensions.isInDayMode


/**
 * Controller for any appearance preferences that could have been
 *   configured in the settings of the app
 */
class AppearanceController(
    private val coreRepository: CoreRepository,
    private val applicationContext: Context
) {

    //region Theme

    var currentTheme: Theme
        get() = coreRepository.theme
        set(value) {
            coreRepository.theme = value
        }

    val isLightMode: Boolean
        get() = currentTheme == Theme.DAY || (currentTheme == Theme.AUTO && applicationContext.isInDayMode())

    //endregion

    //region Bar Animation speed

    var animationSpeed: AnimationSpeed
        get() = coreRepository.animationSpeed
        set(value) {
            coreRepository.animationSpeed = value
        }

    //endregion

}