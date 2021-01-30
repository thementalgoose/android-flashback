package tmg.flashback.controllers

import android.os.Build
import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.data.enums.NotificationRegistration
import tmg.flashback.data.repositories.AppRepository

/**
 * Control the notifications in the app / letting the user know something
 */
class NotificationController(
    private val appRepository: AppRepository,
    private val configurationController: ConfigurationController
) {

    /**
     * Get the banner that we display to the user
     */
    val banner: String?
        get() = configurationController.banner

    /**
     * Notifications channels supported
     */
    val isNotificationChannelsSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    /**
     * Race
     */
    val raceOptInUndecided: Boolean
        get() = appRepository.notificationsRace == null
    var raceOptIn: Boolean
        get() = appRepository.notificationsRace == NotificationRegistration.OPT_IN
        set(value) = when (value) {
            true -> appRepository.notificationsRace = NotificationRegistration.OPT_IN
            false -> appRepository.notificationsRace = NotificationRegistration.OPT_OUT
        }

    /**
     * Qualifying
     */
    val qualifyingOptInUndecided: Boolean
        get() = appRepository.notificationsQualifying == null
    var qualifyingOptIn: Boolean
        get() = appRepository.notificationsQualifying == NotificationRegistration.OPT_IN
        set(value) = when (value) {
            true -> appRepository.notificationsQualifying = NotificationRegistration.OPT_IN
            false -> appRepository.notificationsQualifying = NotificationRegistration.OPT_OUT
        }

    /**
     * Receive misc notifications
     */
    val miscOptInUndecided: Boolean
        get() = appRepository.notificationsMisc == null
    var miscOptIn: Boolean
        get() = appRepository.notificationsMisc == NotificationRegistration.OPT_IN
        set(value) = when (value) {
            true -> appRepository.notificationsMisc = NotificationRegistration.OPT_IN
            false -> appRepository.notificationsMisc = NotificationRegistration.OPT_OUT
        }

    companion object {

        /**
         * Days until the app banner gets displayed at the bottom of the home page
         */
        const val daysUntilDataProvidedBannerMovedToBottom = 5
    }
}