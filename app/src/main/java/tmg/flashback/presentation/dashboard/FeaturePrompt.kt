package tmg.flashback.presentation.dashboard

import androidx.annotation.StringRes
import tmg.flashback.R
import tmg.flashback.strings.R.string

sealed class FeaturePrompt(
    val id: String,
    @StringRes
    val label: Int
) {
    data object RuntimeNotifications: FeaturePrompt(
        id = "feature-runtime",
        label = string.feature_banner_runtime_notifications
    )
}