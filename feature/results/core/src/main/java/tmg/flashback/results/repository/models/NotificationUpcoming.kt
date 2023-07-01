package tmg.flashback.results.repository.models

import tmg.flashback.results.contract.repository.models.NotificationUpcoming

val NotificationUpcoming.prefKey: String
    get() = when (this) {
        NotificationUpcoming.RACE -> "UP_NEXT_NOTIFICATION_RACE"
        NotificationUpcoming.SPRINT -> "UP_NEXT_NOTIFICATION_SPRINT"
//        NotificationUpcoming.SPRINT_QUALIFYING -> TODO()
        NotificationUpcoming.QUALIFYING -> "UP_NEXT_NOTIFICATION_QUALIFYING"
        NotificationUpcoming.FREE_PRACTICE -> "UP_NEXT_NOTIFICATION_FREE_PRACTICE"
        NotificationUpcoming.OTHER -> "UP_NEXT_NOTIFICATION_OTHER"
    }