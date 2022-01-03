package tmg.flashback.notifications.repository

import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.notifications.NotificationRegistration
import tmg.utilities.extensions.toEnum

class NotificationRepository(
    private val preferenceManager: PreferenceManager
) {

    companion object {
        private const val keyNotificationRemoteTopics: String = "NOTIFICATION_REMOTE_TOPICS"
        private const val keyNotificationIds: String = "NOTIFICATION_IDS"
    }

    var remoteNotificationTopics: Set<String>
        get() = preferenceManager.getSet(keyNotificationRemoteTopics, emptySet())
        set(value) = preferenceManager.save(keyNotificationRemoteTopics, value)

    var notificationIds: Set<Int>
        get() = preferenceManager.getSet(keyNotificationIds, setOf())
            .mapNotNull { it.toIntOrNull() }
            .toSet()
        set(value) = preferenceManager.save(keyNotificationIds, value.map { it.toString() }.toSet())

}