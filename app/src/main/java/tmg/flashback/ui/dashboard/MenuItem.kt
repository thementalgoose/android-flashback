package tmg.flashback.ui.dashboard

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.R
import tmg.flashback.ui.components.navigation.NavigationItem

sealed class MenuItem(
    val id: String,
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int
) {
    object Calendar: MenuItem(
        id = "calendar",
        label = R.string.nav_calendar,
        icon = R.drawable.nav_calendar
    )
    object Drivers: MenuItem(
        id = "drivers",
        label = R.string.nav_drivers,
        icon = R.drawable.nav_drivers
    )
    object Constructors: MenuItem(
        id = "constructors",
        label = R.string.nav_constructors,
        icon = R.drawable.nav_constructors
    )
    object Search: MenuItem(
        id = "search",
        label = R.string.nav_search,
        icon = R.drawable.nav_search
    )
    object RSS: MenuItem(
        id = "rss",
        label = R.string.nav_rss,
        icon = R.drawable.nav_rss
    )
    object Settings: MenuItem(
        id = "search",
        label = R.string.nav_settings,
        icon = R.drawable.nav_settings
    )
    object Contact: MenuItem(
        id = "contact",
        label = R.string.nav_contact,
        icon = R.drawable.nav_contact
    )

    fun toNavigationItem(isSelected: Boolean? = null): NavigationItem {
        return NavigationItem(
            id = id,
            label = label,
            icon = icon,
            isSelected = isSelected
        )
    }
}