package tmg.flashback.ui.components.layouts

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

open class DashboardMenuItem(
    val id: String,
    @StringRes
    val label: Int?,
    @DrawableRes
    val icon: Int,
    val isSelected: Boolean? = false,
)