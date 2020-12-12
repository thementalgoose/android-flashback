package tmg.flashback.repo.pref

import org.threeten.bp.LocalDate
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.enums.NotificationRegistration
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.repo.enums.Tooltips

interface PrefCustomisationDB {

    /**
     * Dark mode preference
     */
    var theme: ThemePref

    /**
     * Show the qualifying delta in the layout
     */
    var showQualifyingDelta: Boolean

    /**
     * Bottom sheet expanded default behavior
     */
    var showBottomSheetExpanded: Boolean

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
}