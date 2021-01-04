package tmg.flashback.controllers

import android.os.Build
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.enums.NotificationRegistration
import tmg.flashback.repo.pref.DeviceRepository

/**
 * Control the notifications in the app / letting the user know something
 */
class NotificationController(
        private val deviceRepository: DeviceRepository,
        private val remoteConfigRepository: RemoteConfigRepository
) {

    /**
     * Get the banner that we display to the user
     */
    val banner: String?
        get() = remoteConfigRepository.banner

    /**
     * Notifications channels supported
     */
    val isNotificationChannelsSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    /**
     * Race
     */
    val raceOptInUndecided: Boolean
        get() = deviceRepository.notificationsRace == null
    var raceOptIn: Boolean
        get() = deviceRepository.notificationsRace == NotificationRegistration.OPT_IN
        set(value) = when (value) {
            true -> deviceRepository.notificationsRace = NotificationRegistration.OPT_IN
            false -> deviceRepository.notificationsRace = NotificationRegistration.OPT_OUT
        }

    /**
     * Qualifying
     */
    val qualifyingOptInUndecided: Boolean
        get() = deviceRepository.notificationsQualifying == null
    var qualifyingOptIn: Boolean
        get() = deviceRepository.notificationsQualifying == NotificationRegistration.OPT_IN
        set(value) = when (value) {
            true -> deviceRepository.notificationsQualifying = NotificationRegistration.OPT_IN
            false -> deviceRepository.notificationsQualifying = NotificationRegistration.OPT_OUT
        }

    /**
     * Receive misc notifications
     */
    val miscOptInUndecided: Boolean
        get() = deviceRepository.notificationsMisc == null
    var miscOptIn: Boolean
        get() = deviceRepository.notificationsMisc == NotificationRegistration.OPT_IN
        set(value) = when (value) {
            true -> deviceRepository.notificationsMisc = NotificationRegistration.OPT_IN
            false -> deviceRepository.notificationsMisc = NotificationRegistration.OPT_OUT
        }

    companion object {

        /**
         * Days until the app banner gets displayed at the bottom of the home page
         */
        const val daysUntilDataProvidedBannerMovedToBottom = 5
    }
}