package tmg.flashback.stats.repository.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.stats.R

enum class NotificationChannel(
    val channelId: String,
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int,
    val local: Boolean = true
) {
    RACE("flashback_race", R.string.notification_channel_race, R.drawable.ic_notification_race),
    QUALIFYING("flashback_qualifying", R.string.notification_channel_qualifying, R.drawable.ic_notification_qualifying),
    FREE_PRACTICE("flashback_free_practice", R.string.notification_channel_free_practice, R.drawable.ic_notification_practice),
    SEASON_INFO("flashback_info", R.string.notification_channel_info, R.drawable.ic_notification_season_info)
}