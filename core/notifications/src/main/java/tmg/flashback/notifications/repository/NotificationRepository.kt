package tmg.flashback.notifications.repository

import tmg.flashback.prefs.manager.PreferenceManager

class NotificationRepository(
    private val preferenceManager: PreferenceManager
) {

    companion object {
        private const val keyNotificationRemoteTopics: String = "NOTIFICATION_REMOTE_TOPICS"
        private const val keyNotificationIds: String = "NOTIFICATION_IDS"
        private const val keyNotificationRemoteToken: String = "NOTIFICATION_REMOTE_TOKEN"
    }

    var remoteNotificationToken: String?
        get() = preferenceManager.getString(keyNotificationRemoteToken, null)
        set(value) = preferenceManager.save(keyNotificationRemoteToken, value ?: "")

    var remoteNotificationTopics: Set<String>
        get() = preferenceManager.getSet(keyNotificationRemoteTopics, emptySet())
        set(value) = preferenceManager.save(keyNotificationRemoteTopics, value)

    var notificationIds: Set<Int>
        get() = preferenceManager.getSet(keyNotificationIds, setOf())
            .mapNotNull { it.toIntOrNull() }
            .toSet()
        set(value) = preferenceManager.save(keyNotificationIds, value.map { it.toString() }.toSet())

}