package tmg.flashback.season.usecases

import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.season.repository.HomeRepository
import javax.inject.Inject

class DefaultSeasonUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {

    val defaultSeason: Int
        get() {
            val supportedSeasons = homeRepository.supportedSeasons
            // No season list available, so default to current yeah
            if (supportedSeasons.isEmpty()) {
                return Formula1.currentSeasonYear
            }


            val serverSeason = serverDefaultSeason
            if (homeRepository.keepUserSelectedSeason) {
                val usersLastSeasonSelection = homeRepository.userSelectedSeason ?: serverSeason
                if (supportedSeasons.contains(usersLastSeasonSelection)) {
                    return usersLastSeasonSelection
                } else {
                    // Users last viewed season has been removed
                    //  from supported seasons list. Reset the pref
                    homeRepository.userSelectedSeason = null
                }
            }

            return if (!supportedSeasons.contains(serverDefaultSeason)) {
                supportedSeasons.max()
            } else {
                serverSeason
            }
        }

    private val serverDefaultSeason: Int
        get() = homeRepository.defaultSeason
}