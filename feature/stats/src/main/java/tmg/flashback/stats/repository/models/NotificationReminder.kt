package tmg.flashback.stats.repository.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.stats.R

enum class NotificationReminder(
    val seconds: Int,
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int
) {
    MINUTES_60(3600, R.string.notification_reminder_mins_60, R.drawable.ic_notification_reminder_60),
    MINUTES_30(1800, R.string.notification_reminder_mins_30, R.drawable.ic_notification_reminder_30),
    MINUTES_15(900, R.string.notification_reminder_mins_15, R.drawable.ic_notification_reminder_15),
}