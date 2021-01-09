package tmg.flashback.repo.pref

import org.threeten.bp.LocalDate
import tmg.flashback.repo.enums.NotificationRegistration

/**
 * Storage variables that the user does not manipulate or
 *   interact with.
 *
 * These are tracking variables / automatically set / used under the hood
 */
interface DeviceRepository {

    /**
     * Last booted version
     */
    var lastAppVersion: Int

    /**
     * Automatic crash reporting functionality
     */
    var crashReporting: Boolean

    /**
     * Shake to report functionality
     */
    var shakeToReport: Boolean

    /**
     * Get a unique identifier for the device
     */
    var deviceUdid: String

    /**
     * Remote config has been synchronised
     */
    var remoteConfigSync: Int

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
     * App version for which the release notes had last been seen
     */
    var releaseNotesSeenAppVersion: Int

    //region Notifications

    /**
     * Race Notification preference
     *   null = hasn't been set
     */
    var notificationsRace: NotificationRegistration?

    /**
     * Qualifying Notification preference
     *   null = hasn't been set
     */
    var notificationsQualifying: NotificationRegistration?

    /**
     * Miscellaneous Notification preference
     *   null = hasn't been set
     */
    var notificationsMisc: NotificationRegistration?

    //endregion
}