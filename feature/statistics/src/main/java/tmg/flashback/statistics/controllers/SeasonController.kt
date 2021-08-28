package tmg.flashback.statistics.controllers

import tmg.configuration.controllers.ConfigController
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.statistics.repository.StatisticsRepository
import tmg.flashback.statistics.repository.models.Banner

/**
 * All the preferences surrounding the season, list of all seasons
 */
class SeasonController(
    private val statisticsRepository: StatisticsRepository
) {

    //region Dashboard calendar

    val dashboardCalendar: Boolean
        get() = statisticsRepository.dashboardCalendar

    //endregion

    //region Banner

    val banner: Banner?
        get() = statisticsRepository.banner

    //endregion

    //region Data provided by

    val dataProvidedBy: String?
        get() = statisticsRepository.dataProvidedBy

    //endregion

    //region Provided by location

    var dataProvidedByAtTop: Boolean
        get() = statisticsRepository.dataProvidedByAtTop
        set(value) {
            statisticsRepository.dataProvidedByAtTop = value
        }

    //endregion

    //region Default season shown

    fun clearDefault() {
        statisticsRepository.defaultSeason = null
    }

    fun setUserDefaultSeason(season: Int) {
        defaultSeason = season
    }

    var defaultSeason: Int
        get() {
            val supportedSeasons = statisticsRepository.supportedSeasons
            val userPrefSeason = statisticsRepository.defaultSeason
            val serverSeason = statisticsRepository.serverDefaultYear

            if (supportedSeasons.isEmpty()) {
                return currentSeasonYear
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
            statisticsRepository.defaultSeason = value
        }

    val serverDefaultSeason: Int
        get() = statisticsRepository.serverDefaultYear

    val isUserDefinedValueSet: Boolean
        get() = statisticsRepository.defaultSeason != null

    //endregion

    //region All Seasons

    val supportedSeasons: Set<Int>
        get() = statisticsRepository.supportedSeasons

    //endregion

    //region Showing favourites / all

    var favouritesExpanded: Boolean
        get() = statisticsRepository.showListFavourited
        set(value) {
            statisticsRepository.showListFavourited = value
        }

    var allExpanded: Boolean
        get() = statisticsRepository.showListAll
        set(value) {
            statisticsRepository.showListAll = value
        }

    //endregion

    //region Favourites

    var favouriteSeasons: Set<Int>
        get() = statisticsRepository.favouriteSeasons
        private set(value) {
            statisticsRepository.favouriteSeasons = value
        }

    fun isFavourite(season: Int): Boolean {
        return favouriteSeasons.contains(season)
    }

    fun toggleFavourite(season: Int) {
        val set = favouriteSeasons.toMutableSet()
        when (set.contains(season)) {
            true -> set.remove(season)
            false -> set.add(season)
        }
        favouriteSeasons = set.toSet()
    }

    fun addFavourite(season: Int) {
        val set = favouriteSeasons.toMutableSet()
        set.add(season)
        favouriteSeasons = set.toSet()
    }

    fun removeFavourite(season: Int) {
        val set = favouriteSeasons.toMutableSet()
        set.remove(season)
        favouriteSeasons = set.toSet()
    }

    //endregion


}