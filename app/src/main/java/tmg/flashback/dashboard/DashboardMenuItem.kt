package tmg.flashback.dashboard

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.R

enum class DashboardMenuItem(
    @DrawableRes val icon: Int,
    @StringRes val msg: Int
) {
    TRACK_LIST(R.drawable.ic_menu_tracks, R.string.dashboard_menu_track_list);
//    DRIVERS_CHAMPIONSHIP(R.drawable.ic_menu_drivers, R.string.dashboard_menu_drivers);
//    CONSTRUCTORS_CHAMPIONSHIP(R.drawable.ic_menu_constructors, R.string.dashboard_menu_constructors);
}