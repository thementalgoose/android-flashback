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
internal const val MENU_STYLEGUIDE = "styleguide"
internal const val MENU_ADVERTS = "adverts"

val debugMenuItemList: List<DebugMenuItem> = listOf(
    DebugMenuItem(R.string.debug_menu_debug, R.drawable.debug_list_debug, MENU_DEBUG),
    DebugMenuItem(R.string.debug_menu_styleguide, R.drawable.debug_list_styleguide, MENU_STYLEGUIDE),
    DebugMenuItem(R.string.debug_menu_ads_config, R.drawable.debug_list_adverts, MENU_ADVERTS)
)