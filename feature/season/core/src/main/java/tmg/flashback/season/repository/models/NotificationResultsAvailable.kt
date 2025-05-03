package tmg.flashback.season.repository.models

import tmg.flashback.formula1.model.notifications.NotificationResultsAvailable


val NotificationResultsAvailable.prefKey: String
    get() = when (this) {
        NotificationResultsAvailable.RACE -> "UP_NEXT_NOTIFICATION_RACE_NOTIFY"
        NotificationResultsAvailable.SPRINT -> "UP_NEXT_NOTIFICATION_SPRINT_NOTIFY"
        NotificationResultsAvailable.SPRINT_QUALIFYING -> "UP_NEXT_NOTIFICATION_SPRINT_QUALIFYING_NOTIFY"
        NotificationResultsAvailable.QUALIFYING -> "UP_NEXT_NOTIFICATION_QUALIFYING_NOTIFY"
    }