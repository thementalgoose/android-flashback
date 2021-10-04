package tmg.flashback.statistics.controllers

import tmg.flashback.statistics.repository.StatisticsRepository

class SearchController(
    private val statisticsRepository: StatisticsRepository
) {

    /**
     * Is the search functionality enabled
     */
    val enabled: Boolean by lazy {
        statisticsRepository.searchEnabled
    }
}