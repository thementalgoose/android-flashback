package tmg.flashback.controllers

import tmg.flashback.constants.App.currentYear
import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.data.repositories.AppRepository

/**
 * All the preferences surrounding the season, list of all seasons
 */
class SeasonController(
    private val appRepository: AppRepository,
    private val configurationController: ConfigurationController
) {
    //region Default season shown

    fun clearDefault() {
        appRepository.defaultSeason = null
    }

    fun setUserDefaultSeason(season: Int) {
        defaultSeason = season
    }

    var defaultSeason: Int
        get() {
            val supportedSeasons = configurationController.supportedSeasons
            val userPrefSeason = appRepository.defaultSeason
            val serverSeason = configurationController.defaultSeason

            if (supportedSeasons.isEmpty()) {
                return currentYear
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
            appRepository.defaultSeason = value
        }

    val serverDefaultSeason: Int
        get() = configurationController.defaultSeason

    val isUserDefinedValueSet: Boolean
        get() = appRepository.defaultSeason != null

    //endregion

    //region All Seasons

    val supportedSeasons: Set<Int>
        get() = configurationController.supportedSeasons

    //endregion

    //region Showing favourites / all

    var favouritesExpanded: Boolean
        get() = appRepository.showListFavourited
        set(value) {
            appRepository.showListFavourited = value
        }

    var allExpanded: Boolean
        get() = appRepository.showListAll
        set(value) {
            appRepository.showListAll = value
        }

    //endregion

    //region Favourites

    var favouriteSeasons: Set<Int>
        get() = appRepository.favouriteSeasons
        private set(value) {
            appRepository.favouriteSeasons = value
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