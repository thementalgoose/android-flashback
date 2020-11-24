package tmg.flashback.repo.db

import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.repo.enums.Tooltips

interface PrefsDB {

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
     * Automatic crash reporting functionality
     */
    var crashReporting: Boolean

    /**
     * Shake to report functionality
     */
    var shakeToReport: Boolean

    /**
     * Last booted version
     */
    var lastAppVersion: Int

    /**
     * Are we starting the app up in a new version
     */
    val shouldShowReleaseNotes: Boolean

    /**
     * Get a unique identifier for the device
     */
    var deviceUdid: String

    /**
     * Favourited seasons in the list
     */
    var favouriteSeasons: Set<Int>

    /**
     * Tooltip set of app hints for functional usage
     */
    var tooltips: Set<Tooltips>
}