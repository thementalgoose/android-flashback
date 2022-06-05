package tmg.flashback.stats.ui.weekend

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.stats.R

enum class WeekendNavItem(
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int
) {
    SCHEDULE(
        label = R.string.nav_details,
        icon = R.drawable.nav_schedule
    ),
    QUALIFYING(
        label = R.string.nav_qualifying,
        icon = R.drawable.nav_qualifying
    ),
    SPRINT(
        label = R.string.nav_sprint,
        icon = R.drawable.nav_sprint
    ),
    RACE(
        label = R.string.nav_race,
        icon = R.drawable.nav_race
    ),
    CONSTRUCTOR(
        label = R.string.nav_constructors,
        icon = R.drawable.nav_constructor
    ),
}