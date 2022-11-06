package tmg.flashback.ui.dashboard.compact

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.R
import tmg.flashback.ui.components.navigation.NavigationItem

enum class DashboardNavItem(
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
    );

    fun toNavigationItems(): List<NavigationItem> {
        return DashboardNavItem.values()
            .map {
                NavigationItem(
                    id = it.name,
                    label = it.label,
                    icon = it.icon,
                    isSelected = it == this
                )
            }
    }

    companion object
}

fun DashboardNavItem.Companion.toList(selected: DashboardNavItem?): List<NavigationItem> {
    return DashboardNavItem
        .values()
        .map {
            NavigationItem(
                id = it.name,
                label = it.label,
                icon = it.icon,
                isSelected = it == selected
            )
        }
}

