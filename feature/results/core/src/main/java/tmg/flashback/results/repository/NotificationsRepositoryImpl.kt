package tmg.flashback.results.repository

import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.results.contract.repository.NotificationsRepository
import tmg.flashback.results.contract.repository.models.NotificationUpcoming
import tmg.flashback.results.contract.repository.models.NotificationResultsAvailable
import tmg.flashback.results.repository.models.NotificationReminder
import tmg.flashback.results.repository.models.NotificationSchedule
import tmg.flashback.results.repository.models.prefKey
import tmg.utilities.extensions.toEnum
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationsRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager
): NotificationsRepository {

    companion object {
        private const val keyNotificationReminder: String = "UP_NEXT_NOTIFICATION_REMINDER"

        private const val keyRuntimeNotifications: String = "RUNTIME_NOTIFICATION_PROMPT"
        private const val keyNotificationOnboarding: String = "UP_NEXT_NOTIFICATION_ONBOARDING"
    }


    override fun isEnabled(resultsAvailable: NotificationResultsAvailable): Boolean {
        return preferenceManager.getBoolean(resultsAvailable.prefKey, false)
    }

    override fun setEnabled(upcoming: NotificationResultsAvailable, value: Boolean) {
        preferenceManager.save(upcoming.prefKey, value)
    }

    override fun isUpcomingEnabled(upcoming: NotificationUpcoming): Boolean {
        return preferenceManager.getBoolean(upcoming.prefKey, false)
    }

    override fun setUpcomingEnabled(upcoming: NotificationUpcoming, value: Boolean) {
        preferenceManager.save(upcoming.prefKey, value)
    }



    val notificationSchedule: NotificationSchedule
        get() = NotificationSchedule(
            freePractice = isUpcomingEnabled(NotificationUpcoming.FREE_PRACTICE),
            qualifying = isUpcomingEnabled(NotificationUpcoming.QUALIFYING),
            sprint = isUpcomingEnabled(NotificationUpcoming.SPRINT),
            sprintQualifying = isUpcomingEnabled(NotificationUpcoming.SPRINT_QUALIFYING),
            race = isUpcomingEnabled(NotificationUpcoming.RACE),
            other = isUpcomingEnabled(NotificationUpcoming.OTHER)
        )



    var seenNotificationOnboarding: Boolean
        get() = preferenceManager.getBoolean(keyNotificationOnboarding, false)
        set(value) = preferenceManager.save(keyNotificationOnboarding, value)

    var seenRuntimeNotifications: Boolean
        get() = preferenceManager.getBoolean(keyRuntimeNotifications, false)
        set(value) = preferenceManager.save(keyRuntimeNotifications, value)

    var notificationReminderPeriod: NotificationReminder
        get() = preferenceManager.getInt(keyNotificationReminder, NotificationReminder.MINUTES_30.seconds).toEnum<NotificationReminder> { it.seconds } ?: NotificationReminder.MINUTES_30
        set(value) = preferenceManager.save(keyNotificationReminder, value.seconds)
}