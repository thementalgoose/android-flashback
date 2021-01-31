package tmg.flashback.core.repositories

import org.threeten.bp.LocalDate
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.core.enums.AppHints
import tmg.flashback.core.enums.Theme

interface CoreRepository {

    /**
     * Dark mode preference
     */
    var theme: Theme

    /**
     * Last booted version
     */
    var lastAppVersion: Int

    /**
     * Enable or disable crash reporting
     */
    var crashReporting: Boolean

    /**
     * Shake to report functionality
     */
    var shakeToReport: Boolean

    /**
     * Opt in to anonymous analytics tracking
     */
    var analytics: Boolean

    /**
     * Device UDID
     */
    var deviceUdid: String

    /**
     * App open events
     *   Should be set to first time accessed
     */
    var appFirstBootTime: LocalDate

    /**
     * The number of times the app has been opened
     *   Incremented when app is opened
     */
    var appOpenedCount: Int

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
}