package tmg.flashback.data.repo

import tmg.flashback.formula1.model.notifications.NotificationReminder
import tmg.flashback.formula1.model.notifications.NotificationResultsAvailable
import tmg.flashback.formula1.model.notifications.NotificationSchedule
import tmg.flashback.formula1.model.notifications.NotificationUpcoming
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.utilities.extensions.toEnum
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationsRepositoryImpl @Inject constructor(
    private val notificationIdsRepository: NotificationIdsRepository,
    private val preferenceManager: PreferenceManager
): NotificationsRepository {

    companion object {
        private const val keyNotificationReminder: String = "UP_NEXT_NOTIFICATION_REMINDER"

        private const val keyRuntimeNotifications: String = "RUNTIME_NOTIFICATION_PROMPT"
    }


    override fun isEnabled(resultsAvailable: NotificationResultsAvailable): Boolean {
        return notificationIdsRepository.isChannelEnabled(resultsAvailable.channelId)
    }


    override fun isUpcomingEnabled(upcoming: NotificationUpcoming): Boolean {
        return notificationIdsRepository.isChannelEnabled(upcoming.channelId)
    }



    override val notificationSchedule: NotificationSchedule
        get() = NotificationSchedule(
            freePractice = isUpcomingEnabled(NotificationUpcoming.FREE_PRACTICE),
            qualifying = isUpcomingEnabled(NotificationUpcoming.QUALIFYING),
            sprint = isUpcomingEnabled(NotificationUpcoming.SPRINT),
            sprintQualifying = isUpcomingEnabled(NotificationUpcoming.SPRINT_QUALIFYING),
            race = isUpcomingEnabled(NotificationUpcoming.RACE),
            other = isUpcomingEnabled(NotificationUpcoming.OTHER)
        )



    override var seenRuntimeNotifications: Boolean
        get() = preferenceManager.getBoolean(keyRuntimeNotifications, false)
        set(value) = preferenceManager.save(keyRuntimeNotifications, value)

    override var notificationReminderPeriod: NotificationReminder
        get() = preferenceManager.getInt(keyNotificationReminder, NotificationReminder.MINUTES_30.seconds).toEnum<NotificationReminder> { it.seconds } ?: NotificationReminder.MINUTES_30
        set(value) = preferenceManager.save(keyNotificationReminder, value.seconds)
}