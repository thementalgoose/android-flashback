package tmg.flashback.results.repository.models

import androidx.annotation.DrawableRes
import tmg.flashback.results.R
import tmg.flashback.results.contract.repository.models.NotificationReminder

val NotificationReminder.icon: Int
    @DrawableRes
    get() = when (this) {
        NotificationReminder.MINUTES_60 -> R.drawable.ic_notification_reminder_60
        NotificationReminder.MINUTES_30 -> R.drawable.ic_notification_reminder_30
        NotificationReminder.MINUTES_15 -> R.drawable.ic_notification_reminder_15
    }