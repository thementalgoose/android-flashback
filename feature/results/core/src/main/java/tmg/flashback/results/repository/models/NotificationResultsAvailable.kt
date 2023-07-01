package tmg.flashback.results.repository.models

import tmg.flashback.results.contract.repository.models.NotificationResultsAvailable


val NotificationResultsAvailable.prefKey: String
    get() = when (this) {
        NotificationResultsAvailable.RACE -> "UP_NEXT_NOTIFICATION_RACE_NOTIFY"
        NotificationResultsAvailable.SPRINT -> "UP_NEXT_NOTIFICATION_SPRINT_NOTIFY"
        NotificationResultsAvailable.SPRINT_QUALIFYING -> "UP_NEXT_NOTIFICATION_SPRINT_NOTIFY"
        NotificationResultsAvailable.QUALIFYING -> "UP_NEXT_NOTIFICATION_QUALIFYING_NOTIFY"
    }