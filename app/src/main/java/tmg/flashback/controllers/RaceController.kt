package tmg.flashback.controllers

import tmg.flashback.data.pref.UserRepository

/**
 * Controller for any user preferences configured in the
 *   settings of the app relating to the race overview
 */
class RaceController(
        val userRepository: UserRepository
) {

    var showQualifyingDelta: Boolean
        get() = userRepository.showQualifyingDelta
        set(value) {
            userRepository.showQualifyingDelta = value
        }

    var fadeDNF: Boolean
        get() = userRepository.fadeDNF
        set(value) {
            userRepository.fadeDNF = value
        }

    var showGridPenaltiesInQualifying: Boolean
        get() = userRepository.showGridPenaltiesInQualifying
        set(value) {
            userRepository.showGridPenaltiesInQualifying = value
        }
}