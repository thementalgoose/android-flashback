package tmg.flashback.ui.dashboard.menu

import androidx.compose.ui.graphics.Color

data class MenuSeasonItem(
    val colour: Color,
    val season: Int,
    val isSelected: Boolean,
    val isLast: Boolean = false,
    val isFirst: Boolean = false
)