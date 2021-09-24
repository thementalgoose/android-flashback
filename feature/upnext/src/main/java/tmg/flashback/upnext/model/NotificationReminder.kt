package tmg.flashback.upnext.model

import androidx.annotation.StringRes
import tmg.flashback.upnext.R

enum class NotificationReminder(
    val milliseconds: Int,
    @StringRes
    val label: Int
) {
    MINUTES_60(3600000, R.string.notification_reminder_mins_60),
    MINUTES_30(1800000, R.string.notification_reminder_mins_30),
    MINUTES_15(900000, R.string.notification_reminder_mins_15),
}