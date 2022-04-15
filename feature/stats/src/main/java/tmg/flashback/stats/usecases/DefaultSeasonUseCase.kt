package tmg.flashback.stats.usecases

import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.stats.repository.HomeRepository

class DefaultSeasonUseCase(
    private val homeRepository: HomeRepository
) {
    fun clearDefault() {
        homeRepository.defaultSeason = null
    }

    fun setUserDefaultSeason(season: Int) {
        defaultSeason = season
    }

    var defaultSeason: Int
        get() {
            val supportedSeasons = homeRepository.supportedSeasons
            val userPrefSeason = homeRepository.defaultSeason
            val serverSeason = homeRepository.serverDefaultYear

            if (supportedSeasons.isEmpty()) {
                return Formula1.currentSeasonYear
            }
            if (userPrefSeason != null) {
                if (supportedSeasons.contains(userPrefSeason)) {
                    return userPrefSeason
                }
                else {
                    clearDefault()
                }
            }
            return if (!supportedSeasons.contains(serverSeason)) {
                supportedSeasons.maxOrNull()!!
            } else {
                serverSeason
            }
        }
        private set(value) {
            homeRepository.defaultSeason = value
        }

    val serverDefaultSeason: Int
        get() = homeRepository.serverDefaultYear

    val isUserDefinedValueSet: Boolean
        get() = homeRepository.defaultSeason != null
}