package tmg.flashback.formula1.model.notifications

import androidx.annotation.StringRes
import tmg.flashback.strings.R.string

enum class NotificationResultsAvailable(
    val channelId: String,
    @StringRes
    val channelLabel: Int,
    val remoteSubscriptionTopic: String = channelId
) {
    RACE("notify_race", string.notification_channel_race_notify),
    SPRINT("notify_sprint", string.notification_channel_sprint_notify),
    SPRINT_QUALIFYING("notify_sprint_qualifying", string.notification_channel_sprint_qualifying_notify),
    QUALIFYING("notify_qualifying", string.notification_channel_qualifying_notify)
}