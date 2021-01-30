package tmg.flashback.core.controllers

import tmg.flashback.core.enums.AppHints
import tmg.flashback.core.repositories.CoreRepository

/**
 * Manager to handle app hints around the app
 */
class AppHintsController(
    private val coreRepository: CoreRepository
) {
    /**
     * Show the qualifying long press in the race qualifying data
     */
    var showQualifyingLongPress: Boolean
        get() = !coreRepository.appHints.contains(AppHints.RACE_QUALIFYING_LONG_CLICK)
        set(value) {
            val set = coreRepository.appHints.toMutableSet()
            when (value) {
                true -> set.add(AppHints.RACE_QUALIFYING_LONG_CLICK)
                false -> set.remove(AppHints.RACE_QUALIFYING_LONG_CLICK)
            }
            coreRepository.appHints = set.toSet()
        }

    companion object {

        /**
         * Duration that an app hint shows up on the screen for
         */
        const val appHintDelay: Int = 5000
    }
}