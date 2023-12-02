package tmg.flashback.ui.components.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import tmg.flashback.ui.R
import tmg.flashback.strings.R.string

data class NavigationItem(
    val id: String,
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int,
    val isSelected: Boolean? = false,
)

internal val fakeNavigationItems: List<NavigationItem> = listOf(
    NavigationItem(
        id = "menu",
        label = string.ab_menu,
        icon = R.drawable.ic_nightmode_dark,
        isSelected = true
    ),
    NavigationItem(
        id = "back",
        label = string.ab_back,
        icon = R.drawable.ic_theme_material_you
    ),
    NavigationItem(
        id = "settings",
        label = string.settings_theme_title,
        icon = R.drawable.ic_nightmode_auto
    ),
    NavigationItem(
        id = "light",
        label = string.settings_theme_nightmode_light,
        icon = R.drawable.ic_nightmode_light
    ),
    NavigationItem(
        id = "experiment",
        label = string.settings_experimental,
        icon = R.drawable.ic_theme_default
    )
)