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
        label = R.string.dashboard_tab_calendar,
        icon = R.drawable.dashboard_nav_calendar
    )
    object Drivers: MenuItem(
        id = "drivers",
        label = R.string.dashboard_tab_drivers,
        icon = R.drawable.dashboard_nav_drivers
    )
    object Constructors: MenuItem(
        id = "constructors",
        label = R.string.dashboard_tab_constructors,
        icon = R.drawable.dashboard_nav_constructor
    )


    object Search: MenuItem(
        id = "search",
        label = R.string.dashboard_links_search,
        icon = R.drawable.dashboard_search
    )
    object RSS: MenuItem(
        id = "rss",
        label = R.string.dashboard_links_rss,
        icon = R.drawable.dashboard_rss
    )
    object Settings: MenuItem(
        id = "settings",
        label = R.string.dashboard_links_settings,
        icon = R.drawable.dashboard_settings
    )
    object Contact: MenuItem(
        id = "contact",
        label = R.string.dashboard_links_contact,
        icon = R.drawable.dashboard_contact
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