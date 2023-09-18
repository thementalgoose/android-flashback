package tmg.flashback.ui.dashboard

import androidx.annotation.StringRes
import tmg.flashback.R

sealed class FeaturePrompt(
    val id: String,
    @StringRes
    val label: Int
) {
    data object RuntimeNotifications: FeaturePrompt(
        id = "feature-runtime",
        label = R.string.feature_banner_runtime_notifications
    )
    data object Notifications: FeaturePrompt(
        id = "feature-notifications",
        label = R.string.feature_banner_notifications
    )
}