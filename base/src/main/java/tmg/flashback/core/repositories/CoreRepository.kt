package tmg.flashback.core.repositories

import org.threeten.bp.LocalDate
import tmg.configuration.repository.models.TimeListDisplayType
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.core.enums.AppHints
import tmg.flashback.core.enums.Theme

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
     * App version for which the release notes had last been seen
     */
    var releaseNotesSeenAppVersion: Int

    /**
     * Display list type pref
     */
    var displayListTypePref: TimeListDisplayType
}