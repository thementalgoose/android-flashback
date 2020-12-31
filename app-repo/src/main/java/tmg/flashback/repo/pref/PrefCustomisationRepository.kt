package tmg.flashback.repo.pref

import tmg.flashback.repo.enums.AppHints
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.enums.ThemePref

interface PrefCustomisationRepository {

    /**
     * Dark mode preference
     */
    var theme: ThemePref

    /**
     * Show the qualifying delta in the layout
     */
    var showQualifyingDelta: Boolean

    /**
     * Fade the race results where the driver results in DNF
     */
    var fadeDNF: Boolean

    /**
     * Show the favourited bottom sheet section expanded by default
     */
    var showBottomSheetFavourited: Boolean

    /**
     * Show the all bottom sheet section expanded by default
     */
    var showBottomSheetAll: Boolean

    /**
     * Bar animation preference
     */
    var barAnimation: BarAnimation

    /**
     * Show grid penalties in qualifying
     */
    var showGridPenaltiesInQualifying: Boolean

    /**
     * Favourited seasons in the list
     */
    var favouriteSeasons: Set<Int>
    fun addFavouriteSeason(season: Int) {
        val existing: MutableSet<Int> = favouriteSeasons.toMutableSet()
        existing.add(season)
        favouriteSeasons = existing.toSet()
    }
    fun removeFavouriteSeason(season: Int) {
        val existing: MutableSet<Int> = favouriteSeasons.toMutableSet()
        if (existing.contains(season)) {
            existing.remove(season)
            favouriteSeasons = existing.toSet()
        }
    }

    /**
     * App hints that have been shown in the app
     */
    var appHints: Set<AppHints>
    fun markAppHintShown(appHint: AppHints) {
        val existing: MutableSet<AppHints> = appHints.toMutableSet()
        existing.add(appHint)
        appHints = existing.toSet()
    }
}