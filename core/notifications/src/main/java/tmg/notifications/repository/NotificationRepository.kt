package tmg.notifications.repository

import tmg.core.prefs.manager.PreferenceManager
import tmg.notifications.NotificationRegistration
import tmg.utilities.extensions.toEnum

class NotificationRepository(
    private val preferenceManager: PreferenceManager
) {

    companion object {
        private const val keyNotificationRace: String = "NOTIFICATION_RACE"
        private const val keyNotificationQualifying: String = "NOTIFICATION_QUALIFYING"
        private const val keyNotificationSeasonInfo: String = "NOTIFICATION_SEASON_INFO"
    }

    // TODO: Move these out!
    var enabledRace: NotificationRegistration
        set(value) = preferenceManager.save(keyNotificationRace, value.key)
        get() = preferenceManager
            .getString(keyNotificationRace, "")
            ?.toEnum<NotificationRegistration> {
                it.key
            }
            ?: NotificationRegistration.DEFAULT

    var enabledQualifying: NotificationRegistration
        set(value) = preferenceManager.save(keyNotificationQualifying, value.key)
        get() = preferenceManager
            .getString(keyNotificationQualifying, "")
            ?.toEnum<NotificationRegistration> {
                it.key
            }
            ?: NotificationRegistration.DEFAULT

    var enabledSeasonInfo: NotificationRegistration
        set(value) = preferenceManager.save(keyNotificationSeasonInfo, value.key)
        get() = preferenceManager
            .getString(keyNotificationSeasonInfo, "")
            ?.toEnum<NotificationRegistration> {
                it.key
            }
            ?: NotificationRegistration.DEFAULT

}