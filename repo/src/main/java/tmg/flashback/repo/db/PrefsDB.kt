package tmg.flashback.repo.db

import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.repo.enums.ViewTypePref

interface PrefsDB {

    /**
     * Dark mode preference
     */
    var theme: ThemePref

    /**
     * View type for the home screen
     */
    var viewType: ViewTypePref

    /**
     * Show the qualifying delta in the layout
     */
    var showQualifyingDelta: Boolean

    /**
     * Currently selected year that the app will display when it is opened
     */
    var selectedYear: Int

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
    val isCurrentAppVersionNew: Boolean

    /**
     * Get a unique identifier for the device
     */
    var deviceUdid: String

}