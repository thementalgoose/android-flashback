package tmg.flashback.season.contract.repository

import tmg.flashback.season.contract.repository.models.NotificationReminder
import tmg.flashback.season.contract.repository.models.NotificationResultsAvailable
import tmg.flashback.season.contract.repository.models.NotificationSchedule
import tmg.flashback.season.contract.repository.models.NotificationUpcoming

interface NotificationsRepository {

    fun isUpcomingEnabled(upcoming: NotificationUpcoming): Boolean

    fun isEnabled(resultsAvailable: NotificationResultsAvailable): Boolean

    val notificationSchedule: NotificationSchedule
    var seenRuntimeNotifications: Boolean
    var notificationReminderPeriod: NotificationReminder
}