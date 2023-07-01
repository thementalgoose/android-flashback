package tmg.flashback.results.contract.repository.models

import androidx.annotation.StringRes
import tmg.flashback.results.contract.R

enum class NotificationResultsAvailable(
    val channelId: String,
    @StringRes
    val channelLabel: Int,
    val remoteSubscriptionTopic: String = channelId
) {
    RACE("notify_race", R.string.notification_channel_race_notify),
    SPRINT("notify_sprint", R.string.notification_channel_sprint_notify),
    SPRINT_QUALIFYING("notify_sprint_qualifying", R.string.notification_channel_sprint_qualifying_notify),
    QUALIFYING("notify_qualifying", R.string.notification_channel_qualifying_notify)
}