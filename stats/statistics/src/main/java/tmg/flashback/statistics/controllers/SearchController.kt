package tmg.flashback.statistics.controllers

import tmg.flashback.statistics.repository.HomeRepository

class SearchController(
    private val homeRepository: HomeRepository
) {

    /**
     * Is the search functionality enabled
     */
    val enabled: Boolean by lazy {
        homeRepository.searchEnabled
    }
}