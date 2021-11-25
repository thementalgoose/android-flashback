package tmg.flashback.statistics.repository.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.statistics.R

enum class NotificationChannel(
    val channelId: String,
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int
) {
    RACE("flashback_race", R.string.notification_channel_race, R.drawable.ic_notification_race),
    QUALIFYING("flashback_qualifying", R.string.notification_channel_qualifying, R.drawable.ic_notification_qualifying),
    FREE_PRACTICE("flashback_free_practice", R.string.notification_channel_free_practice, R.drawable.ic_notification_practice),
    SEASON_INFO("race", R.string.notification_channel_info, R.drawable.ic_notification_season_info)
}