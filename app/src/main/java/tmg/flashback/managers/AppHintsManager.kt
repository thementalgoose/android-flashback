package tmg.flashback.managers

import tmg.flashback.repo.enums.AppHints
import tmg.flashback.repo.pref.UserRepository

/**
 * Manager to handle app hints around the app
 */
class AppHintsManager(
        val userRepository: UserRepository
) {
    /**
     * Show the qualifying long press in the race qualifying data
     */
    var showQualifyingLongPress: Boolean
        get() = !userRepository.appHints.contains(AppHints.RACE_QUALIFYING_LONG_CLICK)
        set(value) {
            val set = userRepository.appHints.toMutableSet()
            when (value) {
                true -> set.add(AppHints.RACE_QUALIFYING_LONG_CLICK)
                false -> set.remove(AppHints.RACE_QUALIFYING_LONG_CLICK)
            }
            userRepository.appHints = set.toSet()
        }
}