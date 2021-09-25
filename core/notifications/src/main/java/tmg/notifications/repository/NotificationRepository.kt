package tmg.notifications.repository

import tmg.core.prefs.manager.PreferenceManager
import tmg.notifications.NotificationRegistration
import tmg.utilities.extensions.toEnum

class NotificationRepository(
    private val preferenceManager: PreferenceManager
) {

    companion object {
        private const val keyNotificationSeasonInfo: String = "NOTIFICATION_SEASON_INFO"

        private const val keyNotificationIds: String = "NOTIFICATION_IDS"
    }

    // TODO: To be removed
    var enabledSeasonInfo: NotificationRegistration
        set(value) = preferenceManager.save(keyNotificationSeasonInfo, value.key)
        get() = preferenceManager
            .getString(keyNotificationSeasonInfo, "")
            ?.toEnum<NotificationRegistration> {
                it.key
            }
            ?: NotificationRegistration.DEFAULT


    var notificationIds: Set<Int>
        get() = preferenceManager.getSet(keyNotificationIds, setOf())
            .mapNotNull { it.toIntOrNull() }
            .toSet()
        set(value) = preferenceManager.save(keyNotificationIds, value.map { it.toString() }.toSet())

}