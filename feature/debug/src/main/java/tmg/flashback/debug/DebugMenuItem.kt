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

internal const val MENU_DEBUG = "debug"

val debugMenuItemList: List<DebugMenuItem> = listOf(
    DebugMenuItem(R.string.debug_title, R.drawable.debug_list_item, MENU_DEBUG)
)