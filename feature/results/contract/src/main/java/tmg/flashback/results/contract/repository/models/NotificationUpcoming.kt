package tmg.flashback.results.contract.repository.models

import androidx.annotation.StringRes
import tmg.flashback.results.contract.R

enum class NotificationUpcoming(
    val channelId: String,
    @StringRes
    val channelLabel: Int,
) {
    RACE("flashback_race", R.string.notification_channel_free_practice),
    SPRINT("flashback_sprint", R.string.notification_channel_qualifying),
    SPRINT_QUALIFYING("flashback_sprint_qualifying", R.string.notification_channel_qualifying),
    QUALIFYING("flashback_qualifying", R.string.notification_channel_sprint),
    FREE_PRACTICE("flashback_free_practice", R.string.notification_channel_race),
    OTHER("flashback_info", R.string.notification_channel_info)
}