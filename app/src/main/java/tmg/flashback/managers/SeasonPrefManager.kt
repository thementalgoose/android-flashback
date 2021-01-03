package tmg.flashback.managers

import androidx.annotation.IntRange
import tmg.flashback.currentYear
import tmg.flashback.minimumSupportedYear
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.UserRepository

/**
 * All the preferences surrounding the season, list of all seasons
 */
class SeasonPrefManager(
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

    //region Showing favourites / all

    val defaultFavouritesExpanded: Boolean
        get() = userRepository.showListFavourited

    val defaultAllExpanded: Boolean
        get() = userRepository.showListAll

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