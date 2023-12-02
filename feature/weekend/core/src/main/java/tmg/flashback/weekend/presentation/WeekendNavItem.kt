package tmg.flashback.weekend.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.weekend.R
import tmg.flashback.strings.R.string

enum class WeekendNavItem(
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int
) {
    SCHEDULE(
        label = string.nav_details,
        icon = R.drawable.nav_details
    ),
    QUALIFYING(
        label = string.nav_qualifying,
        icon = R.drawable.nav_qualifying
    ),
    SPRINT_QUALIFYING(
        label = string.nav_sprint_qualifying,
        icon = R.drawable.nav_sprint_qualifying
    ),
    SPRINT(
        label = string.nav_sprint,
        icon = R.drawable.nav_sprint
    ),
    RACE(
        label = string.nav_race,
        icon = R.drawable.nav_race
    ),
}