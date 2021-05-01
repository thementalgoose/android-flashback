package tmg.flashback.core.repositories

import org.threeten.bp.LocalDate
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.core.enums.AppHints
import tmg.flashback.core.enums.Theme
import tmg.flashback.core.model.TimeListDisplayType

interface CoreRepository {

    /**
     * Dark mode preference
     */
    var theme: Theme




    /**
     * App hints that have been shown in the app
     */
    var appHints: Set<AppHints>

    /**
     * Animation Speed preference for widgets / areas of the app that it's applicable
     */
    var animationSpeed: AnimationSpeed

    /**
     * Remote config has been synchronised
     */
    var remoteConfigSync: Int

    /**
     * App version for which the release notes had last been seen
     */
    var releaseNotesSeenAppVersion: Int

    /**
     * Display list type pref
     */
    var displayListTypePref: TimeListDisplayType
}