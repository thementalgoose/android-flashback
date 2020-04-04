package tmg.f1stats.repo.db

import tmg.f1stats.repo.enums.ThemePref
import tmg.f1stats.repo.enums.ViewTypePref

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

}