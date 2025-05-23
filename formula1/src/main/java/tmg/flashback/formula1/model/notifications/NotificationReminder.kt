package tmg.flashback.formula1.model.notifications

import androidx.annotation.StringRes
import tmg.flashback.strings.R.string

enum class NotificationReminder(
    val seconds: Int,
    @StringRes
    val label: Int
) {
    MINUTES_60(3600, string.notification_reminder_mins_60),
    MINUTES_30(1800, string.notification_reminder_mins_30),
    MINUTES_15(900, string.notification_reminder_mins_15),
}