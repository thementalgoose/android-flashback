package tmg.flashback.results.repository.models

import androidx.annotation.DrawableRes
import tmg.flashback.results.R
import tmg.flashback.results.contract.repository.models.NotificationUpcoming

val NotificationUpcoming.prefKey: String
    get() = when (this) {
        NotificationUpcoming.RACE -> "UP_NEXT_NOTIFICATION_RACE"
        NotificationUpcoming.SPRINT -> "UP_NEXT_NOTIFICATION_SPRINT"
        NotificationUpcoming.SPRINT_QUALIFYING -> "UP_NEXT_NOTIFICATION_SPRINT_QUALIFYING"
        NotificationUpcoming.QUALIFYING -> "UP_NEXT_NOTIFICATION_QUALIFYING"
        NotificationUpcoming.FREE_PRACTICE -> "UP_NEXT_NOTIFICATION_FREE_PRACTICE"
        NotificationUpcoming.OTHER -> "UP_NEXT_NOTIFICATION_OTHER"
    }

val NotificationUpcoming.icon: Int
    @DrawableRes
    get() = when (this) {
        NotificationUpcoming.RACE -> R.drawable.ic_notification_race
        NotificationUpcoming.SPRINT -> R.drawable.ic_notification_sprint
        NotificationUpcoming.SPRINT_QUALIFYING -> R.drawable.ic_notification_sprint_qualifying
        NotificationUpcoming.QUALIFYING -> R.drawable.ic_notification_qualifying
        NotificationUpcoming.FREE_PRACTICE -> R.drawable.ic_notification_practice
        NotificationUpcoming.OTHER -> R.drawable.ic_notification_season_info
    }