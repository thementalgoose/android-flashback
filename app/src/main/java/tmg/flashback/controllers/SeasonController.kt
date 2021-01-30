package tmg.flashback.controllers

import tmg.flashback.constants.App.currentYear
import tmg.flashback.data.config.RemoteConfigRepository
import tmg.flashback.data.pref.UserRepository

/**
 * All the preferences surrounding the season, list of all seasons
 */
class SeasonController(
        private val userRepository: UserRepository,
        private val remoteConfigRepository: RemoteConfigRepository
) {
    //region Default season shown

    fun clearDefault() {
        userRepository.defaultSeason = null
    }

    fun setUserDefaultSeason(season: Int) {
        defaultSeason = season
    }

    var defaultSeason: Int
        get() {
            val supportedSeasons = remoteConfigRepository.supportedSeasons
            val userPrefSeason = userRepository.defaultSeason
            val serverSeason = remoteConfigRepository.defaultSeason

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
            userRepository.defaultSeason = value
        }

    val serverDefaultSeason: Int
        get() = remoteConfigRepository.defaultSeason

    val isUserDefinedValueSet: Boolean
        get() = userRepository.defaultSeason != null

    //endregion

    //region All Seasons

    val supportedSeasons: Set<Int>
        get() = remoteConfigRepository.supportedSeasons

    //endregion

    //region Showing favourites / all

    var favouritesExpanded: Boolean
        get() = userRepository.showListFavourited
        set(value) {
            userRepository.showListFavourited = value
        }

    var allExpanded: Boolean
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