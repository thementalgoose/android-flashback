package tmg.flashback.results.usecases

import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.results.repository.HomeRepository
import javax.inject.Inject

class DefaultSeasonUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {

    val defaultSeason: Int
        get() {
            val supportedSeasons = homeRepository.supportedSeasons
            val serverSeason = homeRepository.serverDefaultYear

            if (supportedSeasons.isEmpty()) {
                return Formula1.currentSeasonYear
            }
            return if (!supportedSeasons.contains(serverSeason)) {
                supportedSeasons.maxOrNull()!!
            } else {
                serverSeason
            }
        }

    val serverDefaultSeason: Int
        get() = homeRepository.serverDefaultYear
}