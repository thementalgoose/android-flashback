package tmg.flashback.core.repositories

import tmg.configuration.repository.models.TimeListDisplayType
import tmg.core.ui.model.AnimationSpeed
import tmg.flashback.core.enums.AppHints

interface CoreRepository {




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