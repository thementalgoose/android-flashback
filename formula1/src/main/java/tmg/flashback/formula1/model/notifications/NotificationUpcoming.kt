package tmg.flashback.formula1.model.notifications

import androidx.annotation.StringRes
import tmg.flashback.strings.R.string

enum class NotificationUpcoming(
    val channelId: String,
    @StringRes
    val channelLabel: Int,
) {
    RACE("flashback_race", string.notification_channel_free_practice),
    SPRINT("flashback_sprint", string.notification_channel_qualifying),
    SPRINT_QUALIFYING("flashback_sprint_qualifying", string.notification_channel_sprint_qualifying),
    QUALIFYING("flashback_qualifying", string.notification_channel_sprint),
    FREE_PRACTICE("flashback_free_practice", string.notification_channel_race),
    OTHER("flashback_info", string.notification_channel_info)
}