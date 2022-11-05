package tmg.flashback.stats.repository

import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.stats.repository.models.NotificationReminder
import tmg.flashback.stats.repository.models.NotificationResults
import tmg.flashback.stats.repository.models.NotificationSchedule
import tmg.utilities.extensions.toEnum
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val preferenceManager: PreferenceManager
) {

    companion object {
        private const val keyNotificationRace: String = "UP_NEXT_NOTIFICATION_RACE"
        private const val keyNotificationSprint: String = "UP_NEXT_NOTIFICATION_SPRINT"
        private const val keyNotificationQualifying: String = "UP_NEXT_NOTIFICATION_QUALIFYING"
        private const val keyNotificationFreePractice: String = "UP_NEXT_NOTIFICATION_FREE_PRACTICE"
        private const val keyNotificationOther: String = "UP_NEXT_NOTIFICATION_OTHER"
        private const val keyNotificationReminder: String = "UP_NEXT_NOTIFICATION_REMINDER"

        private const val keyNotificationRaceNotify: String = "UP_NEXT_NOTIFICATION_RACE_NOTIFY"
        private const val keyNotificationSprintNotify: String = "UP_NEXT_NOTIFICATION_SPRINT_NOTIFY"
        private const val keyNotificationQualifyingNotify: String = "UP_NEXT_NOTIFICATION_QUALIFYING_NOTIFY"

        private const val keyRuntimeNotifications: String = "RUNTIME_NOTIFICATION_PROMPT"
        private const val keyNotificationOnboarding: String = "UP_NEXT_NOTIFICATION_ONBOARDING"
    }



    var notificationUpcomingRace: Boolean
        get() = preferenceManager.getBoolean(keyNotificationRace, false)
        set(value) = preferenceManager.save(keyNotificationRace, value)

    var notificationUpcomingSprint: Boolean
        get() = preferenceManager.getBoolean(keyNotificationSprint, false)
        set(value) = preferenceManager.save(keyNotificationSprint, value)

    var notificationUpcomingQualifying: Boolean
        get() = preferenceManager.getBoolean(keyNotificationQualifying, false)
        set(value) = preferenceManager.save(keyNotificationQualifying, value)

    var notificationUpcomingFreePractice: Boolean
        get() = preferenceManager.getBoolean(keyNotificationFreePractice, false)
        set(value) = preferenceManager.save(keyNotificationFreePractice, value)

    var notificationUpcomingOther: Boolean
        get() = preferenceManager.getBoolean(keyNotificationOther, false)
        set(value) = preferenceManager.save(keyNotificationOther, value)

    val notificationSchedule: NotificationSchedule
        get() = NotificationSchedule(
            freePractice = notificationUpcomingFreePractice,
            qualifying = notificationUpcomingQualifying,
            sprint = notificationUpcomingSprint,
            race = notificationUpcomingRace,
            other = notificationUpcomingOther
        )


    var notificationNotifyRace: Boolean
        get() = preferenceManager.getBoolean(keyNotificationRaceNotify, false)
        set(value) = preferenceManager.save(keyNotificationRaceNotify, value)

    var notificationNotifySprint: Boolean
        get() = preferenceManager.getBoolean(keyNotificationSprintNotify, false)
        set(value) = preferenceManager.save(keyNotificationSprintNotify, value)

    var notificationNotifyQualifying: Boolean
        get() = preferenceManager.getBoolean(keyNotificationQualifyingNotify, false)
        set(value) = preferenceManager.save(keyNotificationQualifyingNotify, value)

    val notificationResults: NotificationResults
        get() = NotificationResults(
            qualifying = notificationNotifyQualifying,
            sprint = notificationNotifySprint,
            race = notificationNotifyRace
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