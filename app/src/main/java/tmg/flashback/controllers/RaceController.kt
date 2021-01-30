package tmg.flashback.controllers

import tmg.flashback.data.repositories.AppRepository

/**
 * Controller for any user preferences configured in the
 *   settings of the app relating to the race overview
 */
class RaceController(
        val appRepository: AppRepository
) {

    var showQualifyingDelta: Boolean
        get() = appRepository.showQualifyingDelta
        set(value) {
            appRepository.showQualifyingDelta = value
        }

    var fadeDNF: Boolean
        get() = appRepository.fadeDNF
        set(value) {
            appRepository.fadeDNF = value
        }

    var showGridPenaltiesInQualifying: Boolean
        get() = appRepository.showGridPenaltiesInQualifying
        set(value) {
            appRepository.showGridPenaltiesInQualifying = value
        }
}