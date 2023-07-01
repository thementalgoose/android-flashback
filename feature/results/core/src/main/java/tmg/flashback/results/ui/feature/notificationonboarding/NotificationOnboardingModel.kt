package tmg.flashback.results.ui.feature.notificationonboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.results.contract.repository.models.NotificationUpcoming

data class NotificationOnboardingModel(
    val id: String,
    val channel: NotificationUpcoming,
    @StringRes
    val name: Int,
    @DrawableRes
    val icon: Int
)