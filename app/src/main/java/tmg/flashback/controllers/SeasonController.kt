package tmg.flashback.controllers

import androidx.annotation.IntRange
import tmg.flashback.constants.App.currentYear
import tmg.flashback.constants.Formula1.minimumSupportedYear
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.UserRepository

/**
 * All the preferences surrounding the season, list of all seasons
 */
class SeasonController(
        val userRepository: UserRepository,
        val remoteConfigRepository: RemoteConfigRepository
) {
    //region Default season shown

    /**
     * Undo setting a default year being shown
     */
    fun clearDefault() {
        userRepository.defaultSeason = null
    }

    /**
     * Set a user customised default season to load the app into
     */
    fun setDefaultSeason(@IntRange(from = 1950, to = Long.MAX_VALUE) season: Int) {
        userRepository.defaultSeason = season
    }

    val isDefaultNotSet: Boolean
        get() = userRepository.defaultSeason == null

    var defaultYear: Int
        get() {
            val prefSeason = userRepository.defaultSeason
            if (prefSeason == null || prefSeason < minimumSupportedYear) {
                val remoteConfigSeason = remoteConfigRepository.defaultYear
                if (remoteConfigSeason < minimumSupportedYear) {
                    return currentYear
                }
                return remoteConfigSeason
            }
            return prefSeason
        }
        private set(value) {
            userRepository.defaultSeason = value
        }

    //endregion

    //region All Seasons

    val allSeasons: Set<Int>
        get() = remoteConfigRepository.allSeasons

    //endregion

    //region Showing favourites / all

    var defaultFavouritesExpanded: Boolean
        get() = userRepository.showListFavourited
        set(value) {
            userRepository.showListFavourited = value
        }

    var defaultAllExpanded: Boolean
        get() = userRepository.showListAll
        set(value) {
            userRepository.showListAll = value
        }

    //endregion

    //region Favourites

    var favouriteSeasons: Set<Int>
        get() = userRepository.favouriteSeasons
        private set(value) {
            userRepository.favouriteSeasons = value
        }

    fun isFavourite(season: Int): Boolean {
        return favouriteSeasons.contains(season)
    }

    fun toggle(season: Int) {
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