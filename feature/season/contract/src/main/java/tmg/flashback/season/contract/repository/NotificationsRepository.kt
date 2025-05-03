package tmg.flashback.season.contract.repository

import tmg.flashback.formula1.model.notifications.NotificationReminder
import tmg.flashback.formula1.model.notifications.NotificationResultsAvailable
import tmg.flashback.formula1.model.notifications.NotificationSchedule
import tmg.flashback.formula1.model.notifications.NotificationUpcoming

interface NotificationsRepository {

    fun isUpcomingEnabled(upcoming: NotificationUpcoming): Boolean

    fun isEnabled(resultsAvailable: NotificationResultsAvailable): Boolean

    val notificationSchedule: NotificationSchedule
    var seenRuntimeNotifications: Boolean
    var notificationReminderPeriod: NotificationReminder
}