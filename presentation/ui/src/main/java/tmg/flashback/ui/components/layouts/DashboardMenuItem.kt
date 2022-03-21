package tmg.flashback.ui.components.layouts

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class DashboardMenuItem(
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int
)