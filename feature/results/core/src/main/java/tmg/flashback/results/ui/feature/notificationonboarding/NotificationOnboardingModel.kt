package tmg.flashback.results.ui.feature.notificationonboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.results.repository.models.NotificationChannel

data class NotificationOnboardingModel(
    val id: String,
    val channel: NotificationChannel,
    @StringRes
    val name: Int,
    @DrawableRes
    val icon: Int
)