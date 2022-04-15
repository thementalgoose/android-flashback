package tmg.flashback.ui.components.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.ui.R

open class NavigationItem(
    val id: String,
    @StringRes
    val label: Int?,
    @DrawableRes
    val icon: Int,
    val isSelected: Boolean? = false,
)

internal val fakeNavigationItems: List<NavigationItem> = listOf(
    NavigationItem(
        id = "menu",
        label = R.string.ab_menu,
        icon = R.drawable.ic_nightmode_dark,
        isSelected = true
    ),
    NavigationItem(
        id = "back",
        label = R.string.ab_back,
        icon = R.drawable.ic_theme_material_you
    ),
    NavigationItem(
        id = "settings",
        label = R.string.settings_theme_title,
        icon = R.drawable.ic_bar_animation_medium
    ),
    NavigationItem(
        id = "light",
        label = R.string.settings_theme_nightmode_light,
        icon = R.drawable.ic_nightmode_light
    )
)