package tmg.flashback.stats.ui.feature.notificationonboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.stats.repository.models.NotificationChannel

data class NotificationOnboardingModel(
    val id: String,
    val channel: NotificationChannel,
    @StringRes
    val name: Int,
    @DrawableRes
    val icon: Int
)