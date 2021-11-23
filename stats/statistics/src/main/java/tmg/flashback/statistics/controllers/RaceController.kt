package tmg.flashback.statistics.controllers

import tmg.flashback.statistics.repository.HomeRepository


/**
 * Controller for any user preferences configured in the
 *   settings of the app relating to the race overview
 */
class RaceController(
        private val homeRepository: HomeRepository
) {

    var showQualifyingDelta: Boolean
        get() = false /* statisticsRepository.showQualifyingDelta */
        set(value) {
            homeRepository.showQualifyingDelta = value
        }

    var fadeDNF: Boolean
        get() = true /* statisticsRepository.fadeDNF */
        set(value) {
            homeRepository.fadeDNF = value
        }

    var showGridPenaltiesInQualifying: Boolean
        get() = false /* statisticsRepository.showGridPenaltiesInQualifying */
        set(value) {
            homeRepository.showGridPenaltiesInQualifying = value
        }
}