package tmg.flashback.statistics.controllers

import tmg.flashback.statistics.repository.HomeRepository
import tmg.flashback.statistics.repository.models.Banner

/**
 * All the preferences surrounding the season, list of all seasons
 */
class HomeController(
    private val homeRepository: HomeRepository
) {

    //region Dashboard calendar

    var dashboardDefaultToSchedule: Boolean
        get() = homeRepository.defaultToSchedule
        set(value) { homeRepository.defaultToSchedule = value }

    var dashboardAutoscroll: Boolean
        get() = homeRepository.dashboardAutoscroll
        set(value) { homeRepository.dashboardAutoscroll = value }

    //endregion

    //region Banner

    val banner: Banner?
        get() = homeRepository.banner

    //endregion

    //region Data provided by

    val dataProvidedBy: String?
        get() = homeRepository.dataProvidedBy

    //endregion

    //region Provided by location

    var dataProvidedByAtTop: Boolean
        get() = homeRepository.dataProvidedByAtTop
        set(value) {
            homeRepository.dataProvidedByAtTop = value
        }

    //endregion

    //region All Seasons

    val supportedSeasons: Set<Int>
        get() = homeRepository.supportedSeasons

    //endregion

    //region Showing favourites / all

    var favouritesExpanded: Boolean
        get() = homeRepository.showListFavourited
        set(value) {
            homeRepository.showListFavourited = value
        }

    var allExpanded: Boolean
        get() = homeRepository.showListAll
        set(value) {
            homeRepository.showListAll = value
        }

    //endregion

    //region Favourites

    var favouriteSeasons: Set<Int>
        get() = homeRepository.favouriteSeasons
        private set(value) {
            homeRepository.favouriteSeasons = value
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