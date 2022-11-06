package tmg.flashback.ui.dashboard.expanded

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.R
import tmg.flashback.ui.components.navigation.NavigationItem
import tmg.flashback.ui.dashboard.compact.DashboardNavItem

enum class DashboardExpandedNavItem(
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int,
    val analyticsName: String
) {
    CALENDAR(
        label = R.string.nav_calendar,
        icon = R.drawable.nav_calendar,
        analyticsName = "Calendar"
    ),
    DRIVERS(
        label = R.string.nav_drivers,
        icon = R.drawable.nav_driver,
        analyticsName = "Drivers"
    ),
    CONSTRUCTORS(
        label = R.string.nav_constructors,
        icon = R.drawable.nav_constructor,
        analyticsName = "Constructor"
    ),
    SEARCH(
        label = R.string.nav_search,
        icon = R.drawable.nav_search,
        analyticsName = "Search"
    ),
    RSS(
        label = R.string.nav_rss,
        icon = R.drawable.nav_rss,
        analyticsName = "RSS"
    ),
    SETTINGS(
        label = R.string.nav_settings,
        icon = R.drawable.nav_settings,
        analyticsName = "Settings"
    );

    fun toNavigationItems(): List<NavigationItem> {
        return DashboardExpandedNavItem.values()
            .map {
                NavigationItem(
                    id = it.name,
                    label = it.label,
                    icon = it.icon,
                    isSelected = it == this
                )
            }
    }
}