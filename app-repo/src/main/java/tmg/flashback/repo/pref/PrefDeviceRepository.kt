package tmg.flashback.repo.pref

import org.threeten.bp.LocalDate

interface PrefDeviceRepository {

    /**
     * Last booted version
     */
    var lastAppVersion: Int

    /**
     * Are we starting the app up in a new version
     */
    val shouldShowReleaseNotes: Boolean

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
     * Remote config has been initially synchronised
     */
    var remoteConfigInitialSync: Boolean

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
     * Determine if notifications are supported on this device
     */
    val isNotificationChannelsSupported: Boolean
}