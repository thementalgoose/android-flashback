package tmg.flashback.notifications.repository

import tmg.flashback.notifications.managers.SystemNotificationManager
import tmg.flashback.prefs.manager.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

// TODO: Find a better name for this.
@Singleton
class NotificationIdsRepository @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val systemNotificationManager: SystemNotificationManager,
) {

    companion object {
        private const val keyNotificationRemoteTopics: String = "NOTIFICATION_REMOTE_TOPICS"
        private const val keyNotificationIds: String = "NOTIFICATION_IDS"
        private const val keyNotificationRemoteToken: String = "NOTIFICATION_REMOTE_TOKEN"
    }

    var remoteNotificationToken: String?
        get() = preferenceManager.getString(keyNotificationRemoteToken, null)
        internal set(value) = preferenceManager.save(keyNotificationRemoteToken, value ?: "")

    internal var remoteNotificationTopics: Set<String>
        get() = preferenceManager.getSet(keyNotificationRemoteTopics, emptySet())
        set(value) = preferenceManager.save(keyNotificationRemoteTopics, value)

    var notificationIds: Set<Int>
        get() = preferenceManager.getSet(keyNotificationIds, setOf())
            .mapNotNull { it.toIntOrNull() }
            .toSet()
        internal set(value) = preferenceManager.save(keyNotificationIds, value.map { it.toString() }.toSet())

    fun isChannelEnabled(channelId: String): Boolean {
        return systemNotificationManager.isChannelEnabled(channelId)
    }
}