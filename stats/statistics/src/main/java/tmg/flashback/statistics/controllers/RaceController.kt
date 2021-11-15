package tmg.flashback.statistics.controllers

import tmg.flashback.statistics.repository.StatisticsRepository


/**
 * Controller for any user preferences configured in the
 *   settings of the app relating to the race overview
 */
class RaceController(
        private val statisticsRepository: StatisticsRepository
) {

    var showQualifyingDelta: Boolean
        get() = false /* statisticsRepository.showQualifyingDelta */
        set(value) {
            statisticsRepository.showQualifyingDelta = value
        }

    var fadeDNF: Boolean
        get() = true /* statisticsRepository.fadeDNF */
        set(value) {
            statisticsRepository.fadeDNF = value
        }

    var showGridPenaltiesInQualifying: Boolean
        get() = false /* statisticsRepository.showGridPenaltiesInQualifying */
        set(value) {
            statisticsRepository.showGridPenaltiesInQualifying = value
        }
}