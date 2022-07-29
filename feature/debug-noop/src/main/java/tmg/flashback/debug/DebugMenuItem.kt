package tmg.flashback.debug

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class DebugMenuItem(
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int,
    val id: String
)

val debugMenuItemList: List<DebugMenuItem> = listOf()