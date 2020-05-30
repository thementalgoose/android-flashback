package tmg.flashback.dashboard

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.R

enum class DashboardMenuItem(
    @DrawableRes val icon: Int,
    @StringRes val msg: Int
) {
    SEASONS(R.drawable.ic_menu_tracks, R.string.dashboard_menu_seasons);
}